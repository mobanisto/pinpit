package de.mobanisto.pinpit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.xml.sax.SAXException;

import com.google.common.base.Splitter;

import de.mobanisto.pinpit.tasks.CreateImageAssetsFromMaterialIcon;
import de.mobanisto.pinpit.tasks.CreateProjectComposeForDesktop;
import de.mobanisto.pinpit.util.Keywords;
import de.topobyte.utilities.apache.commons.cli.OptionHelper;
import de.topobyte.utilities.apache.commons.cli.commands.args.CommonsCliArguments;
import de.topobyte.utilities.apache.commons.cli.commands.options.CommonsCliExeOptions;
import de.topobyte.utilities.apache.commons.cli.commands.options.ExeOptions;
import de.topobyte.utilities.apache.commons.cli.commands.options.ExeOptionsFactory;

public class RunCreateProjectComposeForDesktop
{

	private static final String OPTION_OUTPUT = "output";
	private static final String OPTION_PROJECT_NAME = "project-name";
	private static final String OPTION_PROJECT_DESCRIPTION = "description";
	private static final String OPTION_PACKAGE = "package";
	private static final String OPTION_VENDOR_FULL = "vendor-full";
	private static final String OPTION_VENDOR_SHORT = "vendor-short";

	public static ExeOptionsFactory OPTIONS_FACTORY = new ExeOptionsFactory() {

		@Override
		public ExeOptions createOptions()
		{
			Options options = new Options();
			// @formatter:off
			OptionHelper.addL(options, OPTION_OUTPUT, true, true, "directory", "output directory to create project in");
			OptionHelper.addL(options, OPTION_PROJECT_NAME, true, true, "string", "name of the project such as 'Test Project' (camel case, parts seperated by space)");
			OptionHelper.addL(options, OPTION_PROJECT_DESCRIPTION, true, true, "string", "a short project description");
			OptionHelper.addL(options, OPTION_PACKAGE, true, true, "string", "package name such as 'com.example.project.name'");
			OptionHelper.addL(options, OPTION_VENDOR_FULL, true, true, "string", "full vendor name such as 'Yoyodyne Inc'");
			OptionHelper.addL(options, OPTION_VENDOR_SHORT, true, true, "string", "short vendor name such as 'Yoyodyne'");
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
		String projectName = line.getOptionValue(OPTION_PROJECT_NAME);
		String projectDescription = line
				.getOptionValue(OPTION_PROJECT_DESCRIPTION);
		String packageName = line.getOptionValue(OPTION_PACKAGE);
		String vendorFull = line.getOptionValue(OPTION_VENDOR_FULL);
		String vendorShort = line.getOptionValue(OPTION_VENDOR_SHORT);

		List<String> nameParts = Splitter.on(" ").omitEmptyStrings()
				.splitToList(projectName);

		List<String> packageParts = Splitter.on(".").trimResults()
				.omitEmptyStrings().splitToList(packageName);
		PackageDefinition targetPackage = new PackageDefinition(packageParts);

		for (String part : packageParts) {
			if (Keywords.isKeyword(part)) {
				System.out.println(String.format(
						"Error: Invalid part in package name (reserved keyword): '%s'",
						part));
				System.exit(1);
			}
		}

		if (Files.exists(output)) {
			if (!Files.isDirectory(output)) {
				System.out.println(
						"Error: Target location exists, but is not a directory.");
				System.exit(1);
			}
			if (Files.list(output).iterator().hasNext()) {
				System.out.println(
						"Error: Target directory is not empty. I won't overwrite anything.");
				System.exit(1);
			}
		}

		CreateProjectComposeForDesktop createProject = new CreateProjectComposeForDesktop(
				output, targetPackage, vendorFull, vendorShort, nameParts,
				projectDescription);
		createProject.execute();

		CreateImageAssetsFromMaterialIcon createAssets = new CreateImageAssetsFromMaterialIcon(
				config.getInput(), output, config.getColorBackground(),
				config.getColorForeground(), config.getColorDialog(),
				config.getRectSize(), config.getSymbolSize());
		createAssets.execute();
	}

}
