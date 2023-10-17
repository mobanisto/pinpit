package de.mobanisto.pinpit;

import de.topobyte.utilities.apache.commons.cli.commands.ArgumentParser;
import de.topobyte.utilities.apache.commons.cli.commands.ExeRunner;
import de.topobyte.utilities.apache.commons.cli.commands.ExecutionData;
import de.topobyte.utilities.apache.commons.cli.commands.RunnerException;
import de.topobyte.utilities.apache.commons.cli.commands.options.DelegateExeOptions;
import de.topobyte.utilities.apache.commons.cli.commands.options.ExeOptions;
import de.topobyte.utilities.apache.commons.cli.commands.options.ExeOptionsFactory;

public class RunPinpit
{

	public static ExeOptionsFactory OPTIONS_FACTORY = new ExeOptionsFactory() {

		@Override
		public ExeOptions createOptions()
		{
			DelegateExeOptions options = new DelegateExeOptions();
			options.addCommand("create-image-assets-from-material-icon",
					RunCreateImageAssetsFromMaterialIcon.OPTIONS_FACTORY,
					RunCreateImageAssetsFromMaterialIcon.class);
			options.addCommand("create-project-compose-for-desktop",
					RunCreateProjectComposeForDesktop.OPTIONS_FACTORY,
					RunCreateProjectComposeForDesktop.class);
			options.addCommand("create-project-swing",
					RunCreateProjectSwing.OPTIONS_FACTORY,
					RunCreateProjectSwing.class);

			return options;
		}

	};

	public static void main(String[] args) throws RunnerException
	{
		System.out.println("Pinpit version " + Version.getVersion());

		ExeOptions options = OPTIONS_FACTORY.createOptions();
		ArgumentParser parser = new ArgumentParser("pinpit", options);

		ExecutionData data = parser.parse(args);
		if (data != null) {
			ExeRunner.run(data);
		}
	}

}
