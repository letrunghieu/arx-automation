package info.hieule.arx_automation.app

import info.hieule.arx_automation.app.front_end.command.Command
import info.hieule.arx_automation.app.front_end.command.NewAnonymizeRequestCommand
import org.apache.commons.cli.*
import org.litote.kmongo.KMongo

fun buildOptions(): Options {
    val options: Options = Options()

    val subCommandOptionGroup: OptionGroup = OptionGroup()
    subCommandOptionGroup.isRequired = true
    subCommandOptionGroup.addOption(Option.builder(null).longOpt("new").desc("Create new anonymization request").build())
    options.addOptionGroup(subCommandOptionGroup)

    options.addOption(
        Option.builder("d")
            .longOpt("dataset")
            .desc("The name of the dataset")
            .hasArg(true)
            .build()
    )

    options.addOption(
        Option.builder("q")
            .longOpt("qis")
            .desc("The list of quasi-identifier attribute names")
            .hasArg(true)
            .build()
    )

    return options
}

fun main(args: Array<String>) {
    val options: Options = buildOptions()
    val parser: CommandLineParser = DefaultParser()
    val cmd: CommandLine = parser.parse(options, args)

    val mongo = KMongo.createClient("mongodb://root:secret@localhost/?ssl=false")
    val anonymizationDb = mongo.getDatabase("anonymization");

    val commands: ArrayList<Command> = arrayListOf()

    commands.add(NewAnonymizeRequestCommand(anonymizationDb))

    for(command in commands) {
        if (!cmd.hasOption(command.getSubCommandName())) {
            continue;
        }

        command.execute(cmd)
    }
}
