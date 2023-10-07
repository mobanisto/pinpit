package de.mobanisto.pinpit;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

import de.mobanisto.pinpit.tasks.CreateImageAssetsFromMaterialIcon;
import de.topobyte.chromaticity.ColorCode;
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
			return new CommonsCliExeOptions(options, "[options]");
		}

	};

	public static void main(String name, CommonsCliArguments arguments)
			throws Exception
	{
		CommandLine line = arguments.getLine();

		Path input = Paths.get(line.getOptionValue(OPTION_INPUT));
		Path output = Paths.get(line.getOptionValue(OPTION_OUTPUT));
		ColorCode colorBackground = color(
				line.getOptionValue(OPTION_COLOR_BACKGROUND));
		ColorCode colorForeground = color(
				line.getOptionValue(OPTION_COLOR_FOREGROUND));
		ColorCode colorDialog = color(line.getOptionValue(OPTION_COLOR_DIALOG));

		CreateImageAssetsFromMaterialIcon task = new CreateImageAssetsFromMaterialIcon(
				input, output, colorBackground, colorForeground, colorDialog);
		task.execute();
	}

	private static ColorCode color(String value)
	{
		if (value == null) {
			return null;
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
