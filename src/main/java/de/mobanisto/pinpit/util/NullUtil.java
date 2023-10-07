package de.mobanisto.pinpit.util;

public class NullUtil
{

	public static <T> T nullDefault(T value, T def)
	{
		if (value != null) {
			return value;
		} else {
			return def;
		}
	}

}
