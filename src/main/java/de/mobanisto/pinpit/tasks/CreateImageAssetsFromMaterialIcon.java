package de.mobanisto.pinpit.tasks;

import static de.topobyte.inkscape4j.Styles.style;

import java.io.IOException;
import java.nio.file.Path;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.batik.transcoder.TranscoderException;
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
import de.topobyte.inkscape4j.path.FillRule;
import de.topobyte.inkscape4j.path.StringPath;
import de.topobyte.inkscape4j.shape.Rect;
import de.topobyte.inkscape4j.w3c.XmlUtils;

public class CreateImageAssetsFromMaterialIcon extends AbstractCreateImageAssets
{

	private Path input;

	public CreateImageAssetsFromMaterialIcon(Path input, Path output,
			ColorCode colorIconBackground, ColorCode colorIconForeground,
			ColorCode colorDialog, double rectFraction, double symbolFraction,
			OutputPaths outputPaths)
	{
		super(output, colorIconBackground, colorIconForeground, colorDialog,
				rectFraction, symbolFraction, outputPaths);
		this.input = input;
	}

	@Override
	public void execute() throws IOException, ParserConfigurationException,
			SAXException, TranscoderException
	{
		System.out.println("Creating image assets from Material icon");
		super.execute();
	}

	@Override
	SvgFile createIcon(int imageSize, double rectSize, double symbolSize,
			ColorCode colorIconBackground, ColorCode colorIconForeground)
			throws IOException
	{
		Document materialIcon;
		try {
			materialIcon = XmlUtils.parseSvg(input);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			throw new IOException(e);
		}
		String materialPath = getPath(materialIcon);

		return createIcon(materialPath, imageSize, rectSize, symbolSize,
				colorIconBackground, colorIconForeground);
	}

	SvgFile createIcon(String materialPath, int imageSize, double rectFraction,
			double symbolFraction, ColorCode colorIconBackground,
			ColorCode colorIconForeground)
	{
		double rectSize = imageSize * rectFraction;
		double symbolSize = imageSize * symbolFraction;
		double marginRect = (imageSize - rectSize) / 2;
		double marginSymbol = (imageSize - symbolSize) / 2;

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

		Rect rect = new Rect("rect-1", marginRect, marginRect, rectSize,
				rectSize);
		rect.setRx(10);
		rect.setRy(10);
		rect.setStyle(styleRect);
		layerBackground.getObjects().add(rect);

		Group icon = new Group("icon");
		// the Material icon is on a 960x960 stage
		double scale = symbolSize / 960.;
		icon.setTransform(new AffineTransformation().scale(scale, scale)
				.translate(marginSymbol, symbolSize + marginSymbol));

		StringPath pathMaterialIcon = new StringPath("material-icon",
				FillRule.EVEN_ODD, materialPath);
		pathMaterialIcon.setStyle(style(colorIconForeground, null, 1, 1, 1, 2));
		icon.getObjects().add(pathMaterialIcon);
		layerIcon.getObjects().add(icon);

		return svgFile;
	}

	// Material icons only have a single path. We can just extract that
	// and create a filled path from it.
	private static String getPath(Document document)
	{
		NodeList paths = document.getElementsByTagName("path");
		Node item = paths.item(0);
		Attr d = (Attr) item.getAttributes().getNamedItem("d");
		return d.getValue();
	}

}
