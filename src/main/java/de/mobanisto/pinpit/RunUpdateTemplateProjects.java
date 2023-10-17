package de.mobanisto.pinpit;

import static de.mobanisto.pinpit.util.ResourceUtil.resourceAsString;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.file.PathUtils;

import com.google.common.base.Splitter;

import de.topobyte.system.utils.SystemPaths;

public class RunUpdateTemplateProjects
{

	public static void main(String[] args) throws IOException
	{
		System.out.println("Pinpit version " + Version.getVersion());

		update("compose-desktop");
		update("swing");
	}

	private static void update(String name) throws IOException
	{
		Path base = SystemPaths.CWD.resolve("src/main/resources/templates");
		Path dirTarget = base.resolve(name);
		System.out.println("Deleting directory: " + dirTarget);
		PathUtils.delete(dirTarget);

		System.out.println("Copying files");
		String resourceFiles = String.format("templates/%s.files", name);
		String files = resourceAsString(resourceFiles);
		Iterable<String> lines = Splitter.onPattern("\r?\n").trimResults()
				.omitEmptyStrings().split(files);
		for (String filePath : lines) {
			copy(filePath, dirTarget);
		}
	}

	private static Path copy(String filePath, Path output) throws IOException
	{
		Path path = Paths.get(filePath);
		Path withoutPrefix = path.subpath(2, path.getNameCount());
		// resolve target file
		String filename = path.getFileName().toString();
		// store *.java files as *.java to avoid problems in Eclipse with *.java
		// files in src/main/resources.
		if (filename.endsWith(".java")) {
			String altName = filename.substring(0, filename.length() - 5)
					+ ".javax";
			withoutPrefix = withoutPrefix.resolveSibling(altName);
		}
		Path target = output.resolve(withoutPrefix);
		// create parent directory
		Path parent = target.getParent();
		Files.createDirectories(parent);
		// create file
		Path source = SystemPaths.CWD.resolve(filePath);
		Files.copy(source, target);
		return target;
	}

}
