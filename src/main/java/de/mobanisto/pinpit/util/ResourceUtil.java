package de.mobanisto.pinpit.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;

public class ResourceUtil
{

	public static String resourceAsString(String resource) throws IOException
	{
		try (InputStream input = resourceAsStream(resource)) {
			return IOUtils.toString(input, StandardCharsets.UTF_8);
		}
	}

	public static InputStream resourceAsStream(String resource)
	{
		return Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(resource);
	}

}
