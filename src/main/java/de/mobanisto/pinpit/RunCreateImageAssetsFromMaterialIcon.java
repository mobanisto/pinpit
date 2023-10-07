package de.mobanisto.pinpit;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.apache.commons.cli.CommandLine;
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
			Options options = new Options();
			// @formatter:off
			OptionHelper.addL(options, OPTION_INPUT, true, true, "file", "SVG input file");
			OptionHelper.addL(options, OPTION_OUTPUT, true, true, "directory", "output directory to store generated files in");
			OptionHelper.addL(options, OPTION_COLOR_BACKGROUND, true, false, "color", "background color for the icon");
			OptionHelper.addL(options, OPTION_COLOR_FOREGROUND, true, false, "color", "color for tinting the Material icon");
			OptionHelper.addL(options, OPTION_COLOR_DIALOG, true, false, "color", "background used in the Windows installer dialog");
			// @formatter:on

			String nl = System.getProperty("line.separator");

			StringBuilder strb = new StringBuilder();
			strb.append("[options]");
			strb.append(nl + nl);
			strb.append(colorHelpMessage);
			strb.append(nl + nl);

			return new CommonsCliExeOptions(options, strb.toString());
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
				input, output, colorBackground, colorForeground, colorDialog);
		task.execute();
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
