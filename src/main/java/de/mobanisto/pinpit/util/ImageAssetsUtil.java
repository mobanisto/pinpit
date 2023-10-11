package de.mobanisto.pinpit.util;

import static de.topobyte.inkscape4j.Styles.style;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.imageio.ImageIO;

import org.apache.batik.transcoder.TranscoderException;
import org.locationtech.jts.geom.util.AffineTransformation;
import org.w3c.dom.Document;

import de.topobyte.chromaticity.ColorCode;
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
		String png = filename.substring(0, filename.length() - 4) + ".png";
		Path pathPng = pathSvg.resolveSibling(png);
		try (InputStream is = Files.newInputStream(pathSvg)) {
			byte[] imageBytes = BatikUtil.convertSvgToPng(is);
			Files.write(pathPng, imageBytes);
		}
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

			// Convert to 24 bit image without alpha channel
			BufferedImage rgb = new BufferedImage(image.getWidth(),
					image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);

			Graphics2D g2d = rgb.createGraphics();
			g2d.setColor(Color.WHITE);
			g2d.fillRect(0, 0, rgb.getWidth(), rgb.getHeight());
			g2d.drawImage(image, 0, 0, null);
			g2d.dispose();

			ImageIO.write(rgb, "BMP", pathBmp.toFile());
		}
	}

}
