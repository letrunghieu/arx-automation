package info.hieule.arx_automation.app.front_end

import info.hieule.arx_automation.app.front_end.input.dataset.CsvDatasetReader
import org.apache.commons.cli.*
import org.deidentifier.arx.Data

fun buildOptions(): Options {
    val options: Options = Options()

    val subCommandOptionGroup: OptionGroup = OptionGroup()
    subCommandOptionGroup.isRequired = true
    subCommandOptionGroup.addOption(Option.builder(null).longOpt("parse-dataset").desc("Parse the dataset").build())
    options.addOptionGroup(subCommandOptionGroup)

    options.addOption(
        Option.builder("d")
            .longOpt("dataset")
            .desc("The name of the dataset")
            .hasArg(true)
            .build()
    )

    return options
}

fun main(args: Array<String>) {
    val options: Options = buildOptions()
    val parser: CommandLineParser = DefaultParser()
    val cmd: CommandLine = parser.parse(options, args)

    when {
        cmd.hasOption("parse-dataset") -> {
            val dataset: Data = CsvDatasetReader(cmd.getOptionValue('d')).read()

        }
    }
}