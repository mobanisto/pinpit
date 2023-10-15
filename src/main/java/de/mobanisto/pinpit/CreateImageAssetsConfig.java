package de.mobanisto.pinpit;

import java.nio.file.Path;

import de.topobyte.chromaticity.ColorCode;

public class CreateImageAssetsConfig
{

	private Path input;
	private ColorCode colorBackground = null;
	private ColorCode colorForeground = null;
	private ColorCode colorDialog = null;
	private double rectSize;
	private double symbolSize;

	public Path getInput()
	{
		return input;
	}

	public void setInput(Path input)
	{
		this.input = input;
	}

	public ColorCode getColorBackground()
	{
		return colorBackground;
	}

	public void setColorBackground(ColorCode colorBackground)
	{
		this.colorBackground = colorBackground;
	}

	public ColorCode getColorForeground()
	{
		return colorForeground;
	}

	public void setColorForeground(ColorCode colorForeground)
	{
		this.colorForeground = colorForeground;
	}

	public ColorCode getColorDialog()
	{
		return colorDialog;
	}

	public void setColorDialog(ColorCode colorDialog)
	{
		this.colorDialog = colorDialog;
	}

	public double getRectSize()
	{
		return rectSize;
	}

	public void setRectSize(double rectSize)
	{
		this.rectSize = rectSize;
	}

	public double getSymbolSize()
	{
		return symbolSize;
	}

	public void setSymbolSize(double symbolSize)
	{
		this.symbolSize = symbolSize;
	}

}
