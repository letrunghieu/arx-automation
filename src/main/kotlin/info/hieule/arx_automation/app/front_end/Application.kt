package info.hieule.arx_automation.app.front_end

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.MongoCredential
import info.hieule.arx_automation.app.adapter.dataset.MongoDbDatasetWriter
import info.hieule.arx_automation.app.front_end.input.dataset.CsvDatasetReader
import org.apache.commons.cli.*
import org.deidentifier.arx.Data
import org.litote.kmongo.KMongo

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

    val mongo = KMongo.createClient("mongodb://root:secret@localhost/?ssl=false")
    val anonymizationDb = mongo.getDatabase("anonymization");

    when {
        cmd.hasOption("parse-dataset") -> {
            val dataset = CsvDatasetReader(cmd.getOptionValue('d')).read()

            val datasetWriter = MongoDbDatasetWriter(anonymizationDb, "originalDatasets")
            datasetWriter.write(dataset)

            println("Dataset is created with id [${dataset.id}]")
        }
    }
}
