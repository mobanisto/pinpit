package de.mobanisto.pinpit.tasks;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import de.mobanisto.pinpit.PackageDefinition;

public class CreateProjectComposeForDesktop extends AbstractCreateProject
{

	public CreateProjectComposeForDesktop(Path output,
			PackageDefinition targetPackage, String fullVendorName,
			String shortVendorName, List<String> nameParts,
			String projectDescription)
	{
		super(output, "templates/compose-desktop.files", targetPackage,
				fullVendorName, shortVendorName, nameParts, projectDescription);
	}

	@Override
	protected void copyOrEdit(String filePath) throws IOException
	{
		Path sourcePrefix = templatePackage.directory();
		Path relocatedSource = targetPackage.directory();

		Path path = Paths.get(filePath);
		String filename = path.getFileName().toString();

		// strip 'templates/compose-for-desktop' prefix
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
			// This is something like 'desktop/src/main/kotlin'
			Path upToPackage = withoutPrefix.subpath(0,
					withoutPrefix.getNameCount() - sourcePrefix.getNameCount()
							- 1);
			if (filename.equals(mainClass + ".kt")) {
				filename = replacements.get(mainClass) + ".kt";
			} else if (filename.equals("Test" + mainClass + ".kt")) {
				filename = "Test" + replacements.get(mainClass) + ".kt";
			}
			Path relocatedFile = upToPackage.resolve(relocatedSource)
					.resolve(filename);
			edit(filePath, relocatedFile, this::editSourceFile);
		} else if (withoutPrefix.equals(Paths.get("gradlew"))) {
			Path target = copy(filePath, withoutPrefix);
			// make Gradle wrapper executable
			if (withoutPrefix.equals(Paths.get("gradlew"))) {
				target.toFile().setExecutable(true);
			}
		} else if (withoutPrefix
				.equals(Paths.get("desktop/build.gradle.kts"))) {
			edit(filePath, withoutPrefix, this::editBuildGradleKotlin);
		} else if (filename.equals("logback.xml")
				|| filename.equals("logback-test.xml")) {
			edit(filePath, withoutPrefix, this::editLogback);
		} else {
			edit(filePath, withoutPrefix, this::editTextFile);
		}
	}

}
