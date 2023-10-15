package de.mobanisto.pinpit;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.google.common.base.Joiner;

public class PackageDefinition
{

	private List<String> parts;

	public PackageDefinition(List<String> parts)
	{
		this.parts = parts;
	}

	public List<String> getParts()
	{
		return parts;
	}

	public Path directory()
	{
		return Paths.get(Joiner.on("/").join(parts));
	}

	public String dots()
	{
		return Joiner.on(".").join(parts);
	}

}
