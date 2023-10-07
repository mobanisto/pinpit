package de.mobanisto.pinpit.tasks;

import static de.mobanisto.pinpit.util.NullUtil.nullDefault;
import static de.topobyte.inkscape4j.Styles.color;
import static de.topobyte.inkscape4j.Styles.style;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.xml.parsers.ParserConfigurationException;

import org.locationtech.jts.geom.util.AffineTransformation;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import de.topobyte.chromaticity.ColorCode;
import de.topobyte.inkscape4j.Group;
import de.topobyte.inkscape4j.Layer;
import de.topobyte.inkscape4j.Style;
import de.topobyte.inkscape4j.SvgFile;
import de.topobyte.inkscape4j.SvgFileWriting;
import de.topobyte.inkscape4j.path.FillRule;
import de.topobyte.inkscape4j.path.StringPath;
import de.topobyte.inkscape4j.shape.Rect;
import de.topobyte.inkscape4j.w3c.ChildDocument;
import de.topobyte.inkscape4j.w3c.XmlUtils;

public class CreateImageAssetsFromMaterialIcon
{

	private Path input;
	private Path output;
	private ColorCode colorIconBackground;
	private ColorCode colorIconForeground;
	private ColorCode colorDialog;

	public CreateImageAssetsFromMaterialIcon(Path input, Path output,
			ColorCode colorIconBackground, ColorCode colorIconForeground,
			ColorCode colorDialog)
	{
		this.input = input;
		this.output = output;
		this.colorIconBackground = nullDefault(colorIconBackground, color(0x127f73));
		this.colorIconForeground = nullDefault(colorIconForeground, color(0xffffff));
		this.colorDialog = nullDefault(colorDialog, color(0xffe680));
	}

	public void execute()
			throws IOException, ParserConfigurationException, SAXException
	{
		System.out.println("Creating image assets from Material icon");
		System.out.println("input: " + input);
		System.out.println("output: " + output);

		int iconSize = 100;

		Path pathIcon = output.resolve("icon.svg");
		Path pathWindowsBanner = output.resolve("banner.svg");
		Path pathWindowsDialog = output.resolve("dialog.svg");

		Document materialIcon = XmlUtils.parseSvg(input);
		String materialPath = getPath(materialIcon);

		Files.createDirectories(output);
		SvgFile svgIcon = createIcon(materialPath, iconSize,
				colorIconBackground, colorIconForeground);

		try (OutputStream os = Files.newOutputStream(pathIcon)) {
			SvgFileWriting.write(svgIcon, os);
		}

		Document icon = XmlUtils.parseSvg(pathIcon);
		XmlUtils.convertToGroup(icon);
		SvgFile svgWindowsBanner = createWindowsInstallerBanner(icon, iconSize);
		SvgFile svgWindowsDialog = createWindowsInstallerDialog(icon, iconSize,
				colorDialog);

		try (OutputStream os = Files.newOutputStream(pathWindowsBanner)) {
			SvgFileWriting.write(svgWindowsBanner, os);
		}
		try (OutputStream os = Files.newOutputStream(pathWindowsDialog)) {
			SvgFileWriting.write(svgWindowsDialog, os);
		}
	}

	private SvgFile createIcon(String materialPath, int imageSize,
			ColorCode colorIconBackground, ColorCode colorIconForeground)
	{
		double rectSize = imageSize * 0.9;
		double iconSize = imageSize * 0.8;
		double margin = (imageSize - iconSize) / 2;

		SvgFile svgFile = new SvgFile();
		svgFile.setWidth(String.format("%dpx", imageSize));
		svgFile.setHeight(String.format("%dpx", imageSize));

		Layer layerIcon = new Layer("layer-icon");
		Layer layerBackground = new Layer("layer-background");
		layerIcon.setLabel("icon");
		layerBackground.setLabel("background");
		svgFile.getLayers().add(layerBackground);
		svgFile.getLayers().add(layerIcon);

		Style styleRect = new Style();
		styleRect.setFill(colorIconBackground);
		styleRect.setOpacity(1);
		styleRect.setFillOpacity(1);

		Rect rect = new Rect("rect-1", 5, 5, rectSize, rectSize);
		rect.setRx(10);
		rect.setRy(10);
		rect.setStyle(styleRect);
		layerBackground.getObjects().add(rect);

		Group icon = new Group("icon");
		// the Material icon is on a 960x960 stage
		double scale = iconSize / 960.;
		icon.setTransform(new AffineTransformation().scale(scale, scale)
				.translate(margin, iconSize + margin));

		StringPath pathRocket = new StringPath("material-icon",
				FillRule.EVEN_ODD, materialPath);
		pathRocket.setStyle(style(colorIconForeground, null, 1, 1, 1, 2));
		icon.getObjects().add(pathRocket);
		layerIcon.getObjects().add(icon);

		return svgFile;
	}

	private SvgFile createWindowsInstallerBanner(Document svgIcon,
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

	private SvgFile createWindowsInstallerDialog(Document svgIcon,
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

	// Material icons only have a single path. We can just extract that
	// and create a filled path from it.
	private static String getPath(Document document)
			throws ParserConfigurationException, SAXException, IOException
	{
		NodeList paths = document.getElementsByTagName("path");
		Node item = paths.item(0);
		Attr d = (Attr) item.getAttributes().getNamedItem("d");
		return d.getValue();
	}

}
