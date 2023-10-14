package de.mobanisto.pinpit;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Stream;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import com.google.common.base.Joiner;

import de.mobanisto.pinpit.tasks.CreateImageAssetsFromMaterialIcon;
import de.topobyte.chromaticity.ColorCode;
import de.topobyte.chromaticity.WebColors;
import de.topobyte.utilities.apache.commons.cli.OptionHelper;
import de.topobyte.utilities.apache.commons.cli.commands.args.CommonsCliArguments;
import de.topobyte.utilities.apache.commons.cli.commands.options.CommonsCliExeOptions;
import de.topobyte.utilities.apache.commons.cli.commands.options.ExeOptions;
import de.topobyte.utilities.apache.commons.cli.commands.options.ExeOptionsFactory;

public class RunCreateImageAssetsFromMaterialIcon
{

	private static final String OPTION_INPUT = "input";
	private static final String OPTION_OUTPUT = "output";
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

	private static String colorHelpMessage;
	static {
		StringBuilder strb = new StringBuilder();
		strb.append(
				"Colors can be specified using hex notation such as 0xaaff22");
		strb.append(" or by name as one of the web colors: ");

		Stream<String> colorNames = Arrays.asList(WebColors.values()).stream()
				.map(e -> e.name().toLowerCase());
		Joiner.on(", ").appendTo(strb, colorNames.iterator());

		colorHelpMessage = strb.toString();
	}

	public static ExeOptionsFactory OPTIONS_FACTORY = new ExeOptionsFactory() {

		@Override
		public ExeOptions createOptions()
		{
			NumberFormat nf = NumberFormat.getInstance(Locale.US);

			Options options = new Options();
			// @formatter:off
			OptionHelper.addL(options, OPTION_INPUT, true, true, "file", "SVG input file");
			OptionHelper.addL(options, OPTION_OUTPUT, true, true, "directory", "output directory to store generated files in");
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

			String nl = System.getProperty("line.separator");

			return new CommonsCliExeOptions(options, "[options]") {

				@Override
				public void usage(String name)
				{
					String syntax = name + " " + getUsageSuffix();
					HelpFormatter formatter = new HelpFormatter();
					// sort options by order of definition
					formatter.setOptionComparator(null);
					formatter.printHelp(syntax, null, options,
							nl + colorHelpMessage);
				}

			};
		}

	};

	public static void main(String name, CommonsCliArguments arguments)
			throws Exception
	{
		CommandLine line = arguments.getLine();

		Path input = Paths.get(line.getOptionValue(OPTION_INPUT));
		Path output = Paths.get(line.getOptionValue(OPTION_OUTPUT));
		ColorCode colorBackground = null;
		ColorCode colorForeground = null;
		ColorCode colorDialog = null;
		double rectSize;
		double symbolSize;
		try {
			rectSize = parseDouble(line.getOptionValue(OPTION_SIZE_RECT),
					DEFAULT_SIZE_RECT);
			symbolSize = parseDouble(line.getOptionValue(OPTION_SIZE_SYMBOL),
					DEFAULT_SIZE_SYMBOL);
		} catch (NumberFormatException e) {
			System.exit(0);
			return;
		}
		try {
			colorBackground = color(
					line.getOptionValue(OPTION_COLOR_BACKGROUND));
			colorForeground = color(
					line.getOptionValue(OPTION_COLOR_FOREGROUND));
			colorDialog = color(line.getOptionValue(OPTION_COLOR_DIALOG));
		} catch (NumberFormatException e) {
			System.out.println("Error: Invalid color specified.");
			System.out.println();
			System.out.println(colorHelpMessage);
			System.exit(0);
		}

		CreateImageAssetsFromMaterialIcon task = new CreateImageAssetsFromMaterialIcon(
				input, output, colorBackground, colorForeground, colorDialog,
				rectSize, symbolSize);
		task.execute();
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
