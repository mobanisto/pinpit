package de.mobanisto.pinpit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.commons.io.file.PathUtils;
import org.xml.sax.SAXException;

import de.mobanisto.pinpit.tasks.CreateImageAssetsFromMaterialIcon;
import de.topobyte.system.utils.SystemPaths;

public class TestCreateImageAssetsFromMaterialIcon
{

	public static void main(String[] args) throws IOException,
			ParserConfigurationException, SAXException, TranscoderException
	{
		Path cwd = SystemPaths.CWD;
		Path input = cwd.resolve("src/test/resources/rocket.svg");
		Path output = cwd.resolve("testing/rocket");

		if (Files.exists(output)) {
			PathUtils.delete(output);
		}

		CreateImageAssetsFromMaterialIcon task = new CreateImageAssetsFromMaterialIcon(
				input, output, null, null, null);
		task.execute();
	}

}
