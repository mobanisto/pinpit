package de.mobanisto.pinpit.tasks;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.xml.parsers.ParserConfigurationException;

import org.locationtech.jts.geom.util.AffineTransformation;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import de.topobyte.chromaticity.ColorCode;
import de.topobyte.inkscape4j.Group;
import de.topobyte.inkscape4j.Layer;
import de.topobyte.inkscape4j.Style;
import de.topobyte.inkscape4j.SvgFile;
import de.topobyte.inkscape4j.SvgFileWriting;
import de.topobyte.inkscape4j.shape.Rect;
import de.topobyte.inkscape4j.w3c.ChildDocument;
import de.topobyte.inkscape4j.w3c.XmlUtils;

public class CreateImageAssetsFromMaterialIcon
{

	private Path input;
	private Path output;

	public CreateImageAssetsFromMaterialIcon(Path input, Path output)
	{
		this.input = input;
		this.output = output;
	}

	public void execute()
			throws IOException, ParserConfigurationException, SAXException
	{
		System.out.println("Creating image assets from Material icon");
		System.out.println("input: " + input);
		System.out.println("output: " + output);

		Document svgIcon = XmlUtils.parseSvg(input);

		Files.createDirectories(output);

		SvgFile svg = createImage(svgIcon);
		XmlUtils.convertToGroup(svgIcon);

		try (OutputStream os = Files
				.newOutputStream(output.resolve("test.svg"))) {
			SvgFileWriting.write(svg, os);
		}
	}

	private SvgFile createImage(Document svgIcon)
	{
		int imageSize = 100;
		double rectSize = imageSize * 0.9;
		double iconSize = imageSize * 0.8;
		double margin = (imageSize - iconSize) / 2;

		SvgFile svgFile = new SvgFile();
		svgFile.setHeight(String.format("%dpx", imageSize));
		svgFile.setWidth(String.format("%dpx", imageSize));

		Layer layerIcon = new Layer("layer-icon");
		Layer layerBackground = new Layer("layer-background");
		layerIcon.setLabel("icon");
		layerBackground.setLabel("background");
		svgFile.getLayers().add(layerBackground);
		svgFile.getLayers().add(layerIcon);

		Style styleRect = new Style();
		styleRect.setFill(new ColorCode(0x892ca0));
		styleRect.setOpacity(1);
		styleRect.setFillOpacity(1);

		Rect rect = new Rect("rect-1", 5, 5, rectSize, rectSize);
		rect.setRx(10);
		rect.setRy(10);
		rect.setStyle(styleRect);
		layerBackground.getObjects().add(rect);

		ChildDocument childDocument = new ChildDocument(svgIcon);
		Group icon = new Group("icon");
		double scale = iconSize / 960.;
		icon.setTransform(new AffineTransformation().scale(scale, scale)
				.translate(margin, iconSize + margin));
		icon.getObjects().add(childDocument);
		layerIcon.getObjects().add(icon);

		return svgFile;
	}

}
