package de.mobanisto.pinpit.tasks;

import static de.mobanisto.pinpit.tasks.AbstractCreateProject.SourceLanguage.JAVA;

import java.nio.file.Path;
import java.util.List;

import de.mobanisto.pinpit.PackageDefinition;

public class CreateProjectSwing extends AbstractCreateProject
{

	public CreateProjectSwing(Path output, PackageDefinition targetPackage,
			String fullVendorName, String shortVendorName,
			List<String> nameParts, String projectDescription)
	{
		super(output, "templates/swing.files", JAVA, "build.gradle",
				targetPackage, fullVendorName, shortVendorName, nameParts,
				projectDescription);
	}

}
