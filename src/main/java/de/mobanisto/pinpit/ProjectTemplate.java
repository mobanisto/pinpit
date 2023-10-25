package de.mobanisto.pinpit;

import static com.google.common.base.CaseFormat.LOWER_HYPHEN;
import static com.google.common.base.CaseFormat.UPPER_UNDERSCORE;
import static de.mobanisto.pinpit.tasks.CreateProject.SourceLanguage.JAVA;
import static de.mobanisto.pinpit.tasks.CreateProject.SourceLanguage.KOTLIN;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.base.Converter;
import com.google.common.base.Joiner;

import de.mobanisto.pinpit.tasks.CreateProject;

public enum ProjectTemplate {

	SWING("src/main"),
	COMPOSE_FOR_DESKTOP("desktop/src/main");

	private String pathSrcMain;

	ProjectTemplate(String pathSrcMain)
	{
		this.pathSrcMain = pathSrcMain;
	}

	public String getPathSrcMain()
	{
		return pathSrcMain;
	}

	private static Map<String, ProjectTemplate> byName = new HashMap<>();

	static {
		Converter<String, String> converter = UPPER_UNDERSCORE
				.converterTo(LOWER_HYPHEN);
		for (ProjectTemplate template : ProjectTemplate.values()) {
			byName.put(converter.convert(template.name()), template);
		}
	}

	public static ProjectTemplate byName(String name)
	{
		return byName.get(name);
	}

	public static List<String> getNames()
	{
		Converter<String, String> converter = UPPER_UNDERSCORE
				.converterTo(LOWER_HYPHEN);
		List<String> names = new ArrayList<>();
		for (ProjectTemplate template : ProjectTemplate.values()) {
			names.add(converter.convert(template.name()));
		}
		return names;
	}

	public static String listOfNames()
	{
		return Joiner.on(", ").join(getNames().stream().map(e -> "'" + e + "'")
				.collect(Collectors.toList()));
	}

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
