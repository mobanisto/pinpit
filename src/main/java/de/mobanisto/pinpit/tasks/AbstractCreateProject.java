package de.mobanisto.pinpit.tasks;

import static de.mobanisto.pinpit.util.ResourceUtil.resourceAsStream;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
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

import de.mobanisto.pinpit.PackageDefinition;

public class AbstractCreateProject
{

	protected Path output;
	protected PackageDefinition targetPackage;

	protected PackageDefinition templatePackage = new PackageDefinition(
			Arrays.asList("com", "example", "template"));

	protected String mainClass = "TemplateProject";
	protected Map<String, String> replacements = new HashMap<>();

	public AbstractCreateProject(Path output, PackageDefinition targetPackage,
			String fullVendorName, String shortVendorName,
			List<String> nameParts, String projectDescription)
	{
		this.output = output;
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
