package info.hieule.arx_automation.app.front_end.command

import com.fasterxml.jackson.databind.ObjectMapper
import com.mongodb.client.MongoDatabase
import com.rabbitmq.client.ConnectionFactory
import info.hieule.arx_automation.app.adapter.anonymization_request_writer.MongoDbAnonymizationRequestWriter
import info.hieule.arx_automation.app.adapter.anonymization_request_writer.RabbitMqAnonymizationRequestWriter
import info.hieule.arx_automation.app.adapter.dataset_reader.CsvDatasetReader
import info.hieule.arx_automation.app.adapter.dataset_writer.MongoDbDatasetWriter
import info.hieule.arx_automation.shared.models.AnonymizationRequest
import info.hieule.arx_automation.shared.models.Dataset
import info.hieule.arx_automation.shared.models.HierarchyAttributeInfo
import org.apache.commons.cli.CommandLine
import java.nio.file.Path
import java.nio.file.Paths

class NewAnonymizeRequestCommand(private val mongoDatabase: MongoDatabase) : Command {
    override fun getSubCommandName(): String {
        return "new"
    }

    override fun execute(cmd: CommandLine) {
        val datasetName = cmd.getOptionValue("dataset")
        val qiNamesString = cmd.getOptionValue("qis")
        val qiNames = qiNamesString.split(',').map {
            it.trim()
        }

        val originalDataset = this.saveOriginalData(datasetName)

        val hierarchies: ArrayList<HierarchyAttributeInfo> = arrayListOf()
        val validQis = this.filterValidQis(this.getDatasetAttributes(originalDataset), qiNames)
        for (qi in validQis) {
            val hierarchyDataset = this.saveHierarchy(datasetName, qi)
            hierarchies.add(HierarchyAttributeInfo(qi, hierarchyDataset.id.toString()))
        }

        val anonymizationModelConfig: LinkedHashMap<String, Any> = linkedMapOf()
        anonymizationModelConfig.put("k", 5)

        val anonymizationRequest: AnonymizationRequest = this.saveAnonymizationRequest(AnonymizationRequest(
            datasetId = originalDataset.id.toString(),
            hierarchies = hierarchies.toTypedArray(),
            anonymizeModelConfigs = anonymizationModelConfig
        ))

        println("A new anonymization request is created: ${anonymizationRequest.id.toString()}")
    }

    private fun saveOriginalData(datasetName: String): Dataset {
        val datasetFilePath = Paths.get("data", "${datasetName}_int.csv")

        return this.readFromCsvAndUploadToMongodb(datasetFilePath, "originalDatasets")
    }

    private fun getDatasetAttributes(dataset: Dataset): List<String> {
        return dataset.data.first().toList()
    }

    private fun filterValidQis(dataAttributes: List<String>, qiNames: List<String>): List<String> {
        val results: ArrayList<String> = arrayListOf()

        for (qi in qiNames) {
            if (!dataAttributes.contains(qi)) {
                continue
            }

            results.add(qi)
        }

        return results
    }

    private fun saveHierarchy(datasetName: String, attributeName: String): Dataset {
        val datasetFilePath = Paths.get("hierarchies", "${datasetName}_int_hierarchy_${attributeName}.csv")

        return this.readFromCsvAndUploadToMongodb(datasetFilePath, "hierarchies")
    }

    private fun saveAnonymizationRequest(request: AnonymizationRequest): AnonymizationRequest {
        val mongoDbWriter = MongoDbAnonymizationRequestWriter(this.mongoDatabase)
        mongoDbWriter.write(request)

        val objectMapper = ObjectMapper()

        val connectionFactory = ConnectionFactory()
        connectionFactory.host = "localhost"
        connectionFactory.connectionTimeout = 3000
        connectionFactory.newConnection().use { connection ->
            RabbitMqAnonymizationRequestWriter(connection, objectMapper).write(request)
        }

        return request
    }

    private fun readFromCsvAndUploadToMongodb(filePath: Path, collectionName: String): Dataset {
        val dataset = CsvDatasetReader(filePath).read()

        val mongoDbWriter = MongoDbDatasetWriter(this.mongoDatabase, collectionName)
        mongoDbWriter.write(dataset)

        return dataset
    }
}