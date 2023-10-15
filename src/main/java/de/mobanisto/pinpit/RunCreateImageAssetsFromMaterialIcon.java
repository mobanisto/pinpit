package de.mobanisto.pinpit;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.xml.sax.SAXException;

import de.mobanisto.pinpit.tasks.CreateImageAssetsFromMaterialIcon;
import de.topobyte.utilities.apache.commons.cli.OptionHelper;
import de.topobyte.utilities.apache.commons.cli.commands.args.CommonsCliArguments;
import de.topobyte.utilities.apache.commons.cli.commands.options.CommonsCliExeOptions;
import de.topobyte.utilities.apache.commons.cli.commands.options.ExeOptions;
import de.topobyte.utilities.apache.commons.cli.commands.options.ExeOptionsFactory;

public class RunCreateImageAssetsFromMaterialIcon
{

	private static final String OPTION_OUTPUT = "output";

	public static ExeOptionsFactory OPTIONS_FACTORY = new ExeOptionsFactory() {

		@Override
		public ExeOptions createOptions()
		{
			Options options = new Options();
			// @formatter:off
			OptionHelper.addL(options, OPTION_OUTPUT, true, true, "directory", "output directory to store generated files in");
			// @formatter:on
			CreateImageAssetsOptions.add(options);

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
							nl + CreateImageAssetsOptions.COLOR_HELP_MESSAGE);
				}

			};
		}

	};

	public static void main(String name, CommonsCliArguments arguments)
			throws IOException, ParserConfigurationException, SAXException,
			TranscoderException
	{
		CommandLine line = arguments.getLine();

		CreateImageAssetsConfig config;
		try {
			config = CreateImageAssetsOptions.parse(line);
		} catch (CommandLineOptionsException e) {
			System.out.println(
					"Error while parsing command line: " + e.getMessage());
			System.exit(1);
			return;
		}
		Path output = Paths.get(line.getOptionValue(OPTION_OUTPUT));

		CreateImageAssetsFromMaterialIcon task = new CreateImageAssetsFromMaterialIcon(
				config.getInput(), output, config.getColorBackground(),
				config.getColorForeground(), config.getColorDialog(),
				config.getRectSize(), config.getSymbolSize());
		task.execute();
	}

}
