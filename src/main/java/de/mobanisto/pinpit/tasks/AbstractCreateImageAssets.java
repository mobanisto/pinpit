package de.mobanisto.pinpit.tasks;

import static de.mobanisto.pinpit.util.NullUtil.nullDefault;
import static de.topobyte.inkscape4j.Styles.color;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.batik.transcoder.TranscoderException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import de.mobanisto.pinpit.util.ImageAssetsUtil;
import de.topobyte.chromaticity.ColorCode;
import de.topobyte.inkscape4j.SvgFile;
import de.topobyte.inkscape4j.SvgFileWriting;
import de.topobyte.inkscape4j.w3c.XmlUtils;

public abstract class AbstractCreateImageAssets
{

	protected Path output;
	protected ColorCode colorIconBackground;
	protected ColorCode colorIconForeground;
	protected ColorCode colorDialog;
	protected double rectFraction;
	protected double symbolFraction;

	public AbstractCreateImageAssets(Path output, ColorCode colorIconBackground,
			ColorCode colorIconForeground, ColorCode colorDialog,
			double rectFraction, double symbolFraction)
	{
		this.output = output;
		this.colorIconBackground = nullDefault(colorIconBackground,
				color(0x127f73));
		this.colorIconForeground = nullDefault(colorIconForeground,
				color(0xffffff));
		this.colorDialog = nullDefault(colorDialog, color(0xffe680));
		this.rectFraction = rectFraction;
		this.symbolFraction = symbolFraction;
	}

	abstract SvgFile createIcon(int imageSize, double rectSize,
			double symbolSize, ColorCode colorIconBackground,
			ColorCode colorIconForeground) throws IOException;

	public void execute() throws IOException, ParserConfigurationException,
			SAXException, TranscoderException
	{
		System.out.println("output: " + output);

		int iconSize = 100;

		String basename = "icon";
		Path pathIcon = output.resolve(basename + ".svg");
		Path pathIconMacOs = output.resolve(basename + "-macos.svg");
		Path pathWindowsBanner = output.resolve("banner.svg");
		Path pathWindowsDialog = output.resolve("dialog.svg");

		Files.createDirectories(output);
		SvgFile svgIcon = createIcon(iconSize, rectFraction, symbolFraction,
				colorIconBackground, colorIconForeground);

		// For macOS, let the square occupy at most 0.8 of the icon's size.
		// That is what
		// https://developer.apple.com/design/human-interface-guidelines/icons
		// describes (10% margin)
		double maxFractionMac = 0.8;
		double factor = 1;
		if (rectFraction > maxFractionMac) {
			factor = maxFractionMac / rectFraction;
		}
		double rectFractionMac = rectFraction * factor;
		double symbolFractionMac = symbolFraction * factor;

		SvgFile svgIconMacOs = createIcon(iconSize, rectFractionMac,
				symbolFractionMac, colorIconBackground, colorIconForeground);

		try (OutputStream os = Files.newOutputStream(pathIcon)) {
			SvgFileWriting.write(svgIcon, os);
		}

		try (OutputStream os = Files.newOutputStream(pathIconMacOs)) {
			SvgFileWriting.write(svgIconMacOs, os);
		}

		Document icon = XmlUtils.parseSvg(pathIcon);
		XmlUtils.convertToGroup(icon);
		SvgFile svgWindowsBanner = ImageAssetsUtil
				.createWindowsInstallerBanner(icon, iconSize);
		SvgFile svgWindowsDialog = ImageAssetsUtil
				.createWindowsInstallerDialog(icon, iconSize, colorDialog);

		try (OutputStream os = Files.newOutputStream(pathWindowsBanner)) {
			SvgFileWriting.write(svgWindowsBanner, os);
		}
		try (OutputStream os = Files.newOutputStream(pathWindowsDialog)) {
			SvgFileWriting.write(svgWindowsDialog, os);
		}

		// Convert to raster images
		ImageAssetsUtil.convertToPng(pathIcon, 192);
		ImageAssetsUtil.convertToPng(pathIcon, 500);
		ImageAssetsUtil.convertToIco(pathIcon);
		ImageAssetsUtil.convertToIcns(pathIconMacOs, basename);
		ImageAssetsUtil.convertToBmp(pathWindowsBanner);
		ImageAssetsUtil.convertToBmp(pathWindowsDialog);
	}

}
