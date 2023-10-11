package de.mobanisto.pinpit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.commons.io.file.PathUtils;
import org.xml.sax.SAXException;

import de.mobanisto.pinpit.tasks.CreateImageAssetsCircle;
import de.topobyte.system.utils.SystemPaths;

public class TestCreateImageAssetsCircle
{

	public static void main(String[] args) throws IOException,
			ParserConfigurationException, SAXException, TranscoderException
	{
		Path cwd = SystemPaths.CWD;
		Path output = cwd.resolve("testing/circle");

		if (Files.exists(output)) {
			PathUtils.delete(output);
		}

		CreateImageAssetsCircle task = new CreateImageAssetsCircle(output, null,
				null, null);
		task.execute();
	}

}
