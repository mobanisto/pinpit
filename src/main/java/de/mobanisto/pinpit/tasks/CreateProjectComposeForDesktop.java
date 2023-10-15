package de.mobanisto.pinpit.tasks;

import static de.mobanisto.pinpit.util.ResourceUtil.resourceAsStream;
import static de.mobanisto.pinpit.util.ResourceUtil.resourceAsString;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.common.base.Splitter;

public class CreateProjectComposeForDesktop
{

	private Path output;

	public CreateProjectComposeForDesktop(Path output)
	{
		this.output = output;
	}

	public void execute() throws IOException
	{
		String files = resourceAsString("templates/compose-desktop.files");
		Iterable<String> lines = Splitter.onPattern("\r?\n").trimResults()
				.omitEmptyStrings().split(files);
		for (String filePath : lines) {
			copy(filePath);
		}
	}

	private void copy(String filePath) throws IOException
	{
		Path path = Paths.get(filePath);
		// strip 'templates/compose-for-desktop' prefix
		Path withoutPrefix = path.subpath(2, path.getNameCount());
		// resolve target file
		Path target = output.resolve(withoutPrefix);
		// create parent directory
		Path parent = target.getParent();
		Files.createDirectories(parent);
		// create file
		System.out.println(filePath);
		try (InputStream input = resourceAsStream(filePath)) {
			Files.copy(input, target);
		}
		// make Gradle wrapper executable
		if (withoutPrefix.equals(Paths.get("gradlew"))) {
			target.toFile().setExecutable(true);
		}
	}

}
