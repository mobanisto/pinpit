package de.mobanisto.pinpit.tasks;

import static de.mobanisto.pinpit.util.ResourceUtil.resourceAsStream;
import static de.mobanisto.pinpit.util.ResourceUtil.resourceAsString;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

import de.mobanisto.pinpit.PackageDefinition;

public class CreateProject
{

	public static enum SourceLanguage {
		KOTLIN,
		JAVA
	}

	protected Path output;
	protected String resourcesFiles;
	protected String sourceFilesSuffix;
	protected String mainClassExtension;
	protected String pathBuildfile;
	protected PackageDefinition targetPackage;

	protected PackageDefinition templatePackage = new PackageDefinition(
			Arrays.asList("com", "example", "template"));

	protected String mainClass = "TemplateProject";
	protected Map<String, String> replacements = new HashMap<>();
	private SourceLanguage sourceLanguage;

	public CreateProject(Path output, String resourcesFiles,
			SourceLanguage sourceLanguage, String pathBuildfile,
			PackageDefinition targetPackage, String fullVendorName,
			String shortVendorName, List<String> nameParts,
			String projectDescription)
	{
		this.output = output;
		this.resourcesFiles = resourcesFiles;
		this.sourceLanguage = sourceLanguage;
		this.pathBuildfile = pathBuildfile;
		this.targetPackage = targetPackage;

		List<String> lowerParts = nameParts.stream().map(String::toLowerCase)
				.collect(Collectors.toList());

		replacements.put("Template Vendor", fullVendorName);
		replacements.put("TemplateProject", Joiner.on("").join(nameParts));
		replacements.put("Template Project", Joiner.on(" ").join(nameParts));
		replacements.put("Template-Project", Joiner.on("-").join(nameParts));
		replacements.put("template-project", Joiner.on("-").join(lowerParts));
		// project description
		replacements.put("a template project", projectDescription);
		// Windows AUMID
		replacements.put("Vendor.Template.Project",
				shortVendorName + "." + Joiner.on(".").join(nameParts));
		// macOS bundleID
		replacements.put("com.example.template.project",
				targetPackage.dots() + "." + Joiner.on(".").join(lowerParts));
	}

	public void execute() throws IOException
	{
		if (sourceLanguage == SourceLanguage.JAVA) {
			sourceFilesSuffix = "x";
			mainClassExtension = ".java";
		} else if (sourceLanguage == SourceLanguage.KOTLIN) {
			sourceFilesSuffix = null;
			mainClassExtension = ".kt";
		}

		String files = resourceAsString(resourcesFiles);
		Iterable<String> lines = Splitter.onPattern("\r?\n").trimResults()
				.omitEmptyStrings().split(files);
		for (String filePath : lines) {
			copyOrEdit(filePath);
		}
	}

	protected void copyOrEdit(String filePath) throws IOException
	{
		Path sourcePrefix = templatePackage.directory();
		Path relocatedSource = targetPackage.directory();

		Path path = Paths.get(filePath);
		String filename = path.getFileName().toString();

		// strip 'templates/<template name>' prefix
		Path withoutPrefix = path.subpath(2, path.getNameCount());
		boolean isSourceFile = false;
		if (withoutPrefix.getNameCount() > 1) {
			Path dirWithoutPrefix = withoutPrefix.subpath(0,
					withoutPrefix.getNameCount() - 1);
			if (dirWithoutPrefix.endsWith(sourcePrefix)) {
				isSourceFile = true;
			}
		}

		if (filename.endsWith(".bmp") || filename.endsWith(".png")
				|| filename.endsWith(".ico") || filename.endsWith(".icns")
				|| filename.endsWith(".jar")) {
			copy(filePath, withoutPrefix);
		} else if (isSourceFile) {
			// This is something like 'src/main/java' or
			// 'desktop/src/main/kotlin'
			Path upToPackage = withoutPrefix.subpath(0,
					withoutPrefix.getNameCount() - sourcePrefix.getNameCount()
							- 1);
			if (filename.equals(mainClass + mainClassExtension)) {
				filename = replacements.get(mainClass) + mainClassExtension;
			} else if (filename
					.equals("Test" + mainClass + mainClassExtension)) {
				filename = "Test" + replacements.get(mainClass)
						+ mainClassExtension;
			}
			Path relocatedFile = upToPackage.resolve(relocatedSource)
					.resolve(filename);
			String suffix = sourceFilesSuffix != null ? sourceFilesSuffix : "";
			edit(filePath + suffix, relocatedFile, this::editSourceFile);
		} else if (withoutPrefix.equals(Paths.get("gradlew"))) {
			Path target = copy(filePath, withoutPrefix);
			// make Gradle wrapper executable
			if (withoutPrefix.equals(Paths.get("gradlew"))) {
				target.toFile().setExecutable(true);
			}
		} else if (withoutPrefix.equals(Paths.get(pathBuildfile))) {
			if (sourceLanguage == SourceLanguage.JAVA) {
				edit(filePath, withoutPrefix, this::editBuildGradleJava);
			} else if (sourceLanguage == SourceLanguage.KOTLIN) {
				edit(filePath, withoutPrefix, this::editBuildGradleKotlin);
			}
		} else if (filename.equals("logback.xml")
				|| filename.equals("logback-test.xml")) {
			edit(filePath, withoutPrefix, this::editLogback);
		} else {
			edit(filePath, withoutPrefix, this::editTextFile);
		}
	}

	protected String editBuildGradleKotlin(String content)
	{
		String result = content.replace( //
				String.format("mainClass = \"%s.%sKt\"", //
						templatePackage.dots(), mainClass),
				String.format("mainClass = \"%s.%sKt\"", //
						targetPackage.dots(), replacements.get(mainClass)));

		result = result.replace( //
				String.format("packageName = \"%s\"", templatePackage.dots()),
				String.format("packageName = \"%s\"", targetPackage.dots()));

		String templateUuid = "1889DD1C-CD5F-4B43-AC0E-880EC17D5593";
		String generatedUuid = UUID.randomUUID().toString().toUpperCase();
		result = result.replace( //
				String.format("upgradeUuid = \"%s\"", templateUuid),
				String.format("upgradeUuid = \"%s\"", generatedUuid));

		return editTextFile(result);
	}

	protected String editBuildGradleJava(String content)
	{
		String result = content.replace( //
				String.format("mainClass = \"%s.%s\"", //
						templatePackage.dots(), mainClass),
				String.format("mainClass = \"%s.%s\"", //
						targetPackage.dots(), replacements.get(mainClass)));

		result = result.replace( //
				String.format("packageName = \"%s\"", templatePackage.dots()),
				String.format("packageName = \"%s\"", targetPackage.dots()));

		String templateUuid = "1889DD1C-CD5F-4B43-AC0E-880EC17D5593";
		String generatedUuid = UUID.randomUUID().toString().toUpperCase();
		result = result.replace( //
				String.format("upgradeUuid = \"%s\"", templateUuid),
				String.format("upgradeUuid = \"%s\"", generatedUuid));

		return editTextFile(result);
	}

	protected String editSourceFile(String content)
	{
		String result = content.replace("package " + templatePackage.dots(),
				"package " + targetPackage.dots());

		return editTextFile(result);
	}

	protected String editLogback(String content)
	{
		return content.replace(templatePackage.dots(), targetPackage.dots());
	}

	protected String editTextFile(String content)
	{
		String result = content;
		for (Entry<String, String> replacment : replacements.entrySet()) {
			result = result.replace(replacment.getKey(), replacment.getValue());
		}
		return result;
	}

	protected Path copy(String filePath, Path withoutPrefix) throws IOException
	{
		// resolve target file
		Path target = output.resolve(withoutPrefix);
		// create parent directory
		Path parent = target.getParent();
		Files.createDirectories(parent);
		// create file
		try (InputStream input = resourceAsStream(filePath)) {
			Files.copy(input, target);
		}
		return target;
	}

	protected Path edit(String filePath, Path relativeTarget,
			Function<String, String> edit) throws IOException
	{
		// resolve target file
		Path target = output.resolve(relativeTarget);
		// create parent directory
		Path parent = target.getParent();
		Files.createDirectories(parent);
		// create file
		try (InputStream input = resourceAsStream(filePath)) {
			String source = IOUtils.toString(input, StandardCharsets.UTF_8);
			String edited = edit.apply(source);
			Files.writeString(target, edited);
		}
		return target;
	}

}
