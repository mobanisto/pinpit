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

	public AbstractCreateImageAssets(Path output, ColorCode colorIconBackground,
			ColorCode colorIconForeground, ColorCode colorDialog)
	{
		this.output = output;
		this.colorIconBackground = nullDefault(colorIconBackground,
				color(0x127f73));
		this.colorIconForeground = nullDefault(colorIconForeground,
				color(0xffffff));
		this.colorDialog = nullDefault(colorDialog, color(0xffe680));
	}

	abstract SvgFile createIcon(int imageSize, ColorCode colorIconBackground,
			ColorCode colorIconForeground) throws IOException;

	public void execute() throws IOException, ParserConfigurationException,
			SAXException, TranscoderException
	{
		System.out.println("output: " + output);

		int iconSize = 100;

		Path pathIcon = output.resolve("icon.svg");
		Path pathWindowsBanner = output.resolve("banner.svg");
		Path pathWindowsDialog = output.resolve("dialog.svg");

		Files.createDirectories(output);
		SvgFile svgIcon = createIcon(iconSize, colorIconBackground,
				colorIconForeground);

		try (OutputStream os = Files.newOutputStream(pathIcon)) {
			SvgFileWriting.write(svgIcon, os);
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
		ImageAssetsUtil.convertToPng(pathIcon);
		ImageAssetsUtil.convertToIco(pathIcon);
		ImageAssetsUtil.convertToIcns(pathIcon);
		ImageAssetsUtil.convertToBmp(pathWindowsBanner);
		ImageAssetsUtil.convertToBmp(pathWindowsDialog);
	}

}
