package de.mobanisto.pinpit.util;

import com.google.common.base.CaseFormat;
import com.google.common.base.Converter;

public class Test
{

	public static void main(String[] args)
	{
		CaseFormat inputFormat = CaseFormat.UPPER_CAMEL;

		for (CaseFormat format : CaseFormat.values()) {
			Converter<String, String> converter = inputFormat
					.converterTo(format);
			System.out.println(format + ": " + converter.convert("JayPui"));
		}

	}

}
