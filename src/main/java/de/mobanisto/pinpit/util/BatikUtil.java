package de.mobanisto.pinpit.util;

import static org.apache.batik.transcoder.SVGAbstractTranscoder.KEY_HEIGHT;
import static org.apache.batik.transcoder.SVGAbstractTranscoder.KEY_WIDTH;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;

public class BatikUtil
{

	public static byte[] convertSvgToPng(InputStream input)
			throws TranscoderException, IOException
	{
		return convertSvgToPng(input, null, null);
	}

	public static byte[] convertSvgToPng(InputStream input, Float width,
			Float height) throws TranscoderException, IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		TranscoderInput transcoderInput = new TranscoderInput(input);
		TranscoderOutput transcoderOutput = new TranscoderOutput(baos);

		PNGTranscoder pngTranscoder = new PNGTranscoder();
		if (height != null) {
			pngTranscoder.addTranscodingHint(KEY_HEIGHT, height);
		}
		if (width != null) {
			pngTranscoder.addTranscodingHint(KEY_WIDTH, width);
		}
		pngTranscoder.transcode(transcoderInput, transcoderOutput);

		baos.flush();
		return baos.toByteArray();
	}

}