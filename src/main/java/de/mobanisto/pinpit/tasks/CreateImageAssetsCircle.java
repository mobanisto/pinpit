package de.mobanisto.pinpit.tasks;

import java.io.IOException;
import java.nio.file.Path;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.batik.transcoder.TranscoderException;
import org.xml.sax.SAXException;

import de.topobyte.chromaticity.ColorCode;
import de.topobyte.inkscape4j.Layer;
import de.topobyte.inkscape4j.Style;
import de.topobyte.inkscape4j.SvgFile;
import de.topobyte.inkscape4j.shape.Circle;
import de.topobyte.inkscape4j.shape.Rect;

public class CreateImageAssetsCircle extends AbstractCreateImageAssets
{

	public CreateImageAssetsCircle(Path output, ColorCode colorIconBackground,
			ColorCode colorIconForeground, ColorCode colorDialog)
	{
		super(output, colorIconBackground, colorIconForeground, colorDialog);
	}

	@Override
	public void execute() throws IOException, ParserConfigurationException,
			SAXException, TranscoderException
	{
		System.out.println("Creating image assets with circle icon");
		super.execute();
	}

	@Override
	SvgFile createIcon(int imageSize, ColorCode colorIconBackground,
			ColorCode colorIconForeground)
	{
		double rectSize = imageSize * 0.9;
		double iconSize = imageSize * 0.5;

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

		Style styleCircle = new Style();
		styleCircle.setStroke(colorIconForeground);
		styleCircle.setOpacity(1);
		styleCircle.setStrokeOpacity(1);
		styleCircle.setStrokeWidth(5);

		Circle circle = new Circle("circle-1", imageSize / 2f, imageSize / 2f,
				iconSize / 2);
		circle.setStyle(styleCircle);
		layerIcon.getObjects().add(circle);

		return svgFile;
	}

}
