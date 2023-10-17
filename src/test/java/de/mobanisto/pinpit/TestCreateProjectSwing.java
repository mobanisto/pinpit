package de.mobanisto.pinpit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import org.apache.commons.io.file.PathUtils;

import de.mobanisto.pinpit.tasks.CreateProjectSwing;
import de.topobyte.system.utils.SystemPaths;

public class TestCreateProjectSwing
{

	public static void main(String[] args) throws IOException
	{
		Path cwd = SystemPaths.CWD;
		Path output = cwd.resolve("testing/project");

		if (Files.exists(output)) {
			PathUtils.delete(output);
		}

		PackageDefinition targetPackage = new PackageDefinition(
				Arrays.asList("de", "mobanisto"));

		CreateProjectSwing task = new CreateProjectSwing(output, targetPackage,
				"Yoyodyne Inc", "Yoyodyne", Arrays.asList("Foo", "Bar"),
				"a fancy project");
		task.execute();
	}

}
