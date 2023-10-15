package de.mobanisto.pinpit;

import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Stream;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

import com.google.common.base.Joiner;

import de.topobyte.chromaticity.ColorCode;
import de.topobyte.chromaticity.WebColors;
import de.topobyte.utilities.apache.commons.cli.OptionHelper;

public class CreateImageAssetsOptions
{

	private static final String OPTION_INPUT = "input";
	private static final String OPTION_COLOR_BACKGROUND = "color-background";
	private static final String OPTION_COLOR_FOREGROUND = "color-foreground";
	private static final String OPTION_COLOR_DIALOG = "color-dialog";
	private static final String OPTION_SIZE_RECT = "size-rect";
	private static final String OPTION_SIZE_SYMBOL = "size-symbol";

	private static final double DEFAULT_SIZE_RECT = 0.9;
	private static final double DEFAULT_SIZE_SYMBOL = 0.8;

	private static Map<String, ColorCode> webColors = new HashMap<>();
	static {
		for (WebColors color : WebColors.values()) {
			webColors.put(color.name().toLowerCase(), color.color());
		}
	}

	static String NL = System.getProperty("line.separator");

	static String COLOR_HELP_MESSAGE;
	static {
		StringBuilder strb = new StringBuilder();
		strb.append("Colors can be specified using hex notation");
		strb.append(" such as 0xaaff22 or by name");
		strb.append(" as one of the web colors: ");

		Stream<String> colorNames = Arrays.asList(WebColors.values()).stream()
				.map(e -> e.name().toLowerCase());
		Joiner.on(", ").appendTo(strb, colorNames.iterator());

		COLOR_HELP_MESSAGE = strb.toString();
	}

	public static void add(Options options)
	{
		NumberFormat nf = NumberFormat.getInstance(Locale.US);
		// @formatter:off
		OptionHelper.addL(options, OPTION_INPUT, true, true, "file", "Material icon SVG input file");
		OptionHelper.addL(options, OPTION_COLOR_BACKGROUND, true, false, "color", "background color for the icon");
		OptionHelper.addL(options, OPTION_COLOR_FOREGROUND, true, false, "color", "color for tinting the Material icon");
		OptionHelper.addL(options, OPTION_COLOR_DIALOG, true, false, "color", "background used in the Windows installer dialog");
		OptionHelper.addL(options, OPTION_COLOR_FOREGROUND, true, false, "color", "color for tinting the Material icon");
		OptionHelper.addL(options, OPTION_SIZE_RECT, true, false, "double",
				String.format("fraction of the image size for the rectangle (0..1), default: %s",
						nf.format(DEFAULT_SIZE_RECT)));
		OptionHelper.addL(options, OPTION_SIZE_SYMBOL, true, false, "double",
				String.format("fraction of the image size for the symbol (0..1), default: %s",
						nf.format(DEFAULT_SIZE_SYMBOL)));
		// @formatter:on
	}

	public static CreateImageAssetsConfig parse(CommandLine line)
			throws CommandLineOptionsException
	{
		CreateImageAssetsConfig config = new CreateImageAssetsConfig();
		config.setInput(Paths.get(line.getOptionValue(OPTION_INPUT)));
		try {
			config.setRectSize(parseDouble(
					line.getOptionValue(OPTION_SIZE_RECT), DEFAULT_SIZE_RECT));
			config.setSymbolSize(
					parseDouble(line.getOptionValue(OPTION_SIZE_SYMBOL),
							DEFAULT_SIZE_SYMBOL));
		} catch (NumberFormatException e) {
			throw new CommandLineOptionsException(
					"Invalid value specified for rect or symbol size.", e);
		}
		try {
			config.setColorBackground(
					color(line.getOptionValue(OPTION_COLOR_BACKGROUND)));
			config.setColorForeground(
					color(line.getOptionValue(OPTION_COLOR_FOREGROUND)));
			config.setColorDialog(
					color(line.getOptionValue(OPTION_COLOR_DIALOG)));
		} catch (NumberFormatException e) {
			throw new CommandLineOptionsException(
					"Invalid color specified." + NL + NL + COLOR_HELP_MESSAGE,
					e);
		}
		return config;
	}

	private static double parseDouble(String value, double defaultValue)
	{
		if (value == null) {
			return defaultValue;
		}
		return Double.parseDouble(value);
	}

	private static ColorCode color(String value)
	{
		if (value == null) {
			return null;
		}
		if (webColors.containsKey(value.toLowerCase())) {
			return webColors.get(value.toLowerCase());
		}
		int hex = parseHex(value);
		return new ColorCode(hex);
	}

	private static int parseHex(String arg)
	{
		if (arg.startsWith("0x")) {
			arg = arg.substring(2);
		}
		return Integer.parseInt(arg, 16);
	}

}
