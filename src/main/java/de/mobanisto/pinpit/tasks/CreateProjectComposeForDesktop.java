package de.mobanisto.pinpit.tasks;

import static de.mobanisto.pinpit.tasks.AbstractCreateProject.SourceLanguage.KOTLIN;

import java.nio.file.Path;
import java.util.List;

import de.mobanisto.pinpit.PackageDefinition;

public class CreateProjectComposeForDesktop extends AbstractCreateProject
{

	public CreateProjectComposeForDesktop(Path output,
			PackageDefinition targetPackage, String fullVendorName,
			String shortVendorName, List<String> nameParts,
			String projectDescription)
	{
		super(output, "templates/compose-desktop.files", KOTLIN,
				"desktop/build.gradle.kts", targetPackage, fullVendorName,
				shortVendorName, nameParts, projectDescription);
	}

}
