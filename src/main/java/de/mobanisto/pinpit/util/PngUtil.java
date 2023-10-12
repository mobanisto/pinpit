package de.mobanisto.pinpit.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.hjg.pngj.FilterType;
import ar.com.hjg.pngj.IImageLine;
import ar.com.hjg.pngj.PngReader;
import ar.com.hjg.pngj.PngWriter;

public class PngUtil
{

	final static Logger logger = LoggerFactory.getLogger(PngUtil.class);

	public static byte[] findOptimalEncoding(byte[] imageBytes)
	{
		return findOptimalEncoding(imageBytes, 9);
	}

	public static byte[] findOptimalEncoding(byte[] imageBytes, int cLevel)
	{
		FilterType[] filterTypes = new FilterType[] { FilterType.FILTER_NONE,
				FilterType.FILTER_SUB, FilterType.FILTER_UP,
				FilterType.FILTER_AVERAGE, FilterType.FILTER_PAETH };

		byte[][] results = new byte[filterTypes.length][];

		for (int i = 0; i < filterTypes.length; i++) {
			FilterType filterType = filterTypes[i];
			try {
				results[i] = encode(imageBytes, cLevel, filterType);
			} catch (IOException e) {
				logger.warn("unable create image copy", e);
			}
		}

		int bestIndex = -1;
		int bestSize = Integer.MAX_VALUE;
		for (int i = 0; i < filterTypes.length; i++) {
			byte[] data = results[i];
			int size = data.length;
			if (logger.isDebugEnabled()) {
				FilterType filterType = filterTypes[i];
				String filterName = filterType.toString();
				logger.debug(String.format("%s: %d", filterName, size));
			}
			if (size < bestSize) {
				bestSize = size;
				bestIndex = i;
			}
		}

		byte[] data = results[bestIndex];
		return data;
	}

	public static byte[] encode(byte[] imageBytes, int cLevel,
			FilterType filterType) throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		BufferedOutputStream bos = new BufferedOutputStream(baos);
		encode(imageBytes, bos, cLevel, filterType);
		bos.close();
		return baos.toByteArray();
	}

	public static void encode(byte[] imageBytes, OutputStream os, int cLevel,
			FilterType filterType) throws IOException
	{
		InputStream input = new ByteArrayInputStream(imageBytes);
		PngReader reader = new PngReader(input);
		PngWriter writer = new PngWriter(os, reader.imgInfo);
		writer.setFilterType(filterType);
		writer.setCompLevel(cLevel);
		writer.copyChunksFrom(reader.getChunksList());
		for (int row = 0; row < reader.imgInfo.rows; row++) {
			IImageLine l1 = reader.readRow(row);
			writer.writeRow(l1, row);
		}
		reader.end();
		writer.end();
		input.close();
	}

}
