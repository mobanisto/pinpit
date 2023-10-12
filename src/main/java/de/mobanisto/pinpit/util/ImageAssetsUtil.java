package de.mobanisto.pinpit.util;

import static de.topobyte.inkscape4j.Styles.style;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.batik.transcoder.TranscoderException;
import org.locationtech.jts.geom.util.AffineTransformation;
import org.w3c.dom.Document;

import com.github.gino0631.icns.IcnsBuilder;
import com.github.gino0631.icns.IcnsIcons;
import com.github.gino0631.icns.IcnsType;

import de.topobyte.bmp4j.codec.BMPEncoder;
import de.topobyte.chromaticity.ColorCode;
import de.topobyte.ico4j.codec.ICOEncoder;
import de.topobyte.inkscape4j.Group;
import de.topobyte.inkscape4j.Layer;
import de.topobyte.inkscape4j.SvgFile;
import de.topobyte.inkscape4j.shape.Rect;
import de.topobyte.inkscape4j.w3c.ChildDocument;

public class ImageAssetsUtil
{

	public static SvgFile createWindowsInstallerBanner(Document svgIcon,
			int originalIconSize)
	{
		int width = 493;
		int height = 58;
		double iconSize = height * 0.9;
		double margin = (height - iconSize) / 2;

		SvgFile svgFile = new SvgFile();
		svgFile.setWidth(String.format("%dpx", width));
		svgFile.setHeight(String.format("%dpx", height));

		Layer layerIcon = new Layer("layer-icon");
		Layer layerBackground = new Layer("layer-background");
		layerIcon.setLabel("icon");
		layerBackground.setLabel("background");
		svgFile.getLayers().add(layerBackground);
		svgFile.getLayers().add(layerIcon);

		ChildDocument childDocument = new ChildDocument(svgIcon);
		Group icon = new Group("icon");
		double scale = iconSize / originalIconSize;
		icon.setTransform(new AffineTransformation().scale(scale, scale)
				.translate(width - margin - iconSize, margin));
		icon.getObjects().add(childDocument);
		layerIcon.getObjects().add(icon);

		return svgFile;
	}

	public static SvgFile createWindowsInstallerDialog(Document svgIcon,
			int originalIconSize, ColorCode colorSide)
	{
		int width = 493;
		int height = 312;
		int bannerHeight = 58;
		double iconSize = bannerHeight * 0.9;

		SvgFile svgFile = new SvgFile();
		svgFile.setWidth(String.format("%dpx", width));
		svgFile.setHeight(String.format("%dpx", height));

		Layer layerIcon = new Layer("layer-icon");
		Layer layerBackground = new Layer("layer-background");
		layerIcon.setLabel("icon");
		layerBackground.setLabel("background");
		svgFile.getLayers().add(layerBackground);
		svgFile.getLayers().add(layerIcon);

		Rect rect = new Rect("rect1", 0, 0, 164, height);
		rect.setStyle(style(colorSide, null, 1, 1, 1, 2));
		layerBackground.getObjects().add(rect);

		ChildDocument childDocument = new ChildDocument(svgIcon);
		Group icon = new Group("icon");
		double scale = iconSize / originalIconSize;
		icon.setTransform(new AffineTransformation().scale(scale, scale)
				.translate(89, 19));
		icon.getObjects().add(childDocument);
		layerIcon.getObjects().add(icon);

		return svgFile;
	}

	public static void convertToPng(Path pathSvg)
			throws IOException, TranscoderException
	{
		String filename = pathSvg.getFileName().toString();
		String basename = filename.substring(0, filename.length() - 4);

		float size = 500;

		Path pathPng = pathSvg.resolveSibling(basename + ".png");
		try (InputStream is = Files.newInputStream(pathSvg)) {
			byte[] imageBytes = BatikUtil.convertSvgToPng(is, size, size);
			Files.write(pathPng, imageBytes);
		}
	}

	public static void convertToIco(Path pathSvg)
			throws IOException, TranscoderException
	{
		String filename = pathSvg.getFileName().toString();
		String basename = filename.substring(0, filename.length() - 4);

		List<BufferedImage> images = new ArrayList<>();
		for (int size : new int[] { 16, 32, 48, 256 }) {
			try (InputStream is = Files.newInputStream(pathSvg)) {
				byte[] imageBytes = BatikUtil.convertSvgToPng(is, (float) size,
						(float) size);
				images.add(ImageIO.read(new ByteArrayInputStream(imageBytes)));
			}
		}

		Path pathIco = pathSvg.resolveSibling(basename + ".ico");
		boolean[] compress = new boolean[] { false, false, false, true };
		ICOEncoder.write(images, compress, pathIco.toFile());
	}

	public static void convertToBmp(Path pathSvg)
			throws IOException, TranscoderException
	{
		String filename = pathSvg.getFileName().toString();
		String bmp = filename.substring(0, filename.length() - 4) + ".bmp";
		Path pathBmp = pathSvg.resolveSibling(bmp);
		try (InputStream is = Files.newInputStream(pathSvg)) {
			byte[] imageBytes = BatikUtil.convertSvgToPng(is);
			BufferedImage image = ImageIO
					.read(new ByteArrayInputStream(imageBytes));
			BMPEncoder.write(image, pathBmp.toFile());
		}
	}

	public static void convertToIcns(Path pathSvg)
			throws IOException, TranscoderException
	{
		String filename = pathSvg.getFileName().toString();
		String basename = filename.substring(0, filename.length() - 4);

		Path pathIcns = pathSvg.resolveSibling(basename + ".icns");

		Map<Integer, byte[]> images = new HashMap<>();

		for (int size : new int[] { 16, 32, 64, 128, 256, 512, 1024 }) {
			try (InputStream is = Files.newInputStream(pathSvg)) {
				byte[] imageBytes = BatikUtil.convertSvgToPng(is, (float) size,
						(float) size);
				images.put(size, imageBytes);
			}
		}

		try (IcnsBuilder builder = IcnsBuilder.getInstance()) {
			builder.add(IcnsType.ICNS_16x16_JPEG_PNG_IMAGE,
					new ByteArrayInputStream(images.get(16)));
			builder.add(IcnsType.ICNS_16x16_2X_JPEG_PNG_IMAGE,
					new ByteArrayInputStream(images.get(32)));
			builder.add(IcnsType.ICNS_32x32_JPEG_PNG_IMAGE,
					new ByteArrayInputStream(images.get(32)));
			builder.add(IcnsType.ICNS_32x32_2X_JPEG_PNG_IMAGE,
					new ByteArrayInputStream(images.get(64)));
			builder.add(IcnsType.ICNS_128x128_JPEG_PNG_IMAGE,
					new ByteArrayInputStream(images.get(128)));
			builder.add(IcnsType.ICNS_128x128_2X_JPEG_PNG_IMAGE,
					new ByteArrayInputStream(images.get(256)));
			builder.add(IcnsType.ICNS_256x256_JPEG_PNG_IMAGE,
					new ByteArrayInputStream(images.get(256)));
			builder.add(IcnsType.ICNS_256x256_2X_JPEG_PNG_IMAGE,
					new ByteArrayInputStream(images.get(512)));
			builder.add(IcnsType.ICNS_512x512_JPEG_PNG_IMAGE,
					new ByteArrayInputStream(images.get(512)));
			builder.add(IcnsType.ICNS_1024x1024_2X_JPEG_PNG_IMAGE,
					new ByteArrayInputStream(images.get(1024)));

			try (IcnsIcons builtIcons = builder.build()) {
				try (OutputStream os = Files.newOutputStream(pathIcns)) {
					builtIcons.writeTo(os);
				}
			}
		}
	}

}
