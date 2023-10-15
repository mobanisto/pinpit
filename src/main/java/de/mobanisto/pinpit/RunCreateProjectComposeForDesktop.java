package de.mobanisto.pinpit;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import de.mobanisto.pinpit.tasks.CreateImageAssetsFromMaterialIcon;
import de.mobanisto.pinpit.tasks.CreateProjectComposeForDesktop;
import de.topobyte.utilities.apache.commons.cli.OptionHelper;
import de.topobyte.utilities.apache.commons.cli.commands.args.CommonsCliArguments;
import de.topobyte.utilities.apache.commons.cli.commands.options.CommonsCliExeOptions;
import de.topobyte.utilities.apache.commons.cli.commands.options.ExeOptions;
import de.topobyte.utilities.apache.commons.cli.commands.options.ExeOptionsFactory;

public class RunCreateProjectComposeForDesktop
{

	private static final String OPTION_OUTPUT = "output";
	private static final String OPTION_PROJECT_NAME = "project-name";
	private static final String OPTION_PACKAGE = "package";

	public static ExeOptionsFactory OPTIONS_FACTORY = new ExeOptionsFactory() {

		@Override
		public ExeOptions createOptions()
		{
			Options options = new Options();
			// @formatter:off
			OptionHelper.addL(options, OPTION_OUTPUT, true, true, "directory", "output directory to store generated files in");
			OptionHelper.addL(options, OPTION_PROJECT_NAME, true, true, "string", "name of the project such as 'Test Project'");
			OptionHelper.addL(options, OPTION_PACKAGE, true, true, "string", "package name such as 'com.example.project.name'");
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
			throws IOException
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

		CreateImageAssetsFromMaterialIcon task1 = new CreateImageAssetsFromMaterialIcon(
				config.getInput(), output, config.getColorBackground(),
				config.getColorForeground(), config.getColorDialog(),
				config.getRectSize(), config.getSymbolSize());

		CreateProjectComposeForDesktop task = new CreateProjectComposeForDesktop(
				output);
		task.execute();
	}

}
