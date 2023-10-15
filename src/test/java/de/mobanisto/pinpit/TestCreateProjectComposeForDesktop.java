package de.mobanisto.pinpit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.io.file.PathUtils;

import de.mobanisto.pinpit.tasks.CreateProjectComposeForDesktop;
import de.topobyte.system.utils.SystemPaths;

public class TestCreateProjectComposeForDesktop
{

	public static void main(String[] args) throws IOException
	{
		Path cwd = SystemPaths.CWD;
		Path output = cwd.resolve("testing/project");

		if (Files.exists(output)) {
			PathUtils.delete(output);
		}

		CreateProjectComposeForDesktop task = new CreateProjectComposeForDesktop(
				output);
		task.execute();
	}

}
