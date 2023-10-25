package de.mobanisto.pinpit;

import static de.mobanisto.pinpit.tasks.CreateProject.SourceLanguage.JAVA;
import static de.mobanisto.pinpit.tasks.CreateProject.SourceLanguage.KOTLIN;

import java.nio.file.Path;
import java.util.List;

import de.mobanisto.pinpit.tasks.CreateProject;

public enum ProjectTemplate {

	SWING,
	COMPOSE_FOR_DESKTOP;

	public static CreateProject creationTask(ProjectTemplate template,
			Path output, PackageDefinition targetPackage, String fullVendorName,
			String shortVendorName, List<String> nameParts,
			String projectDescription)
	{
		if (template == SWING) {
			return new CreateProject(output, "templates/swing.files", JAVA,
					"build.gradle", targetPackage, fullVendorName,
					shortVendorName, nameParts, projectDescription);
		} else if (template == COMPOSE_FOR_DESKTOP) {
			return new CreateProject(output, "templates/compose-desktop.files",
					KOTLIN, "desktop/build.gradle.kts", targetPackage,
					fullVendorName, shortVendorName, nameParts,
					projectDescription);
		}
		return null;
	}

}
