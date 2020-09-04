package info.hieule.arx_automation.app.springboot.adapter.results_consumer

import com.mongodb.client.MongoDatabase
import info.hieule.arx_automation.app.adapter.anonymization_request_writer.MongoDbAnonymizationRequestWriter
import info.hieule.arx_automation.app.adapter.dataset_writer.MongoDbDatasetWriter
import info.hieule.arx_automation.app.springboot.consumer.configuration.ConsumerConfiguration
import info.hieule.arx_automation.ports.ResultsConsumer
import info.hieule.arx_automation.shared.models.*
import org.deidentifier.arx.ARXLattice
import org.deidentifier.arx.ARXResult
import org.deidentifier.arx.Data
import org.litote.kmongo.getCollection
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import kotlin.math.roundToInt

@Component
class DefaultResultsConsumer(
    private val mongoDatabase: MongoDatabase,
    private val rabbitTemplate: RabbitTemplate
) : ResultsConsumer {

    @Value("\${app.rabbitmq.exchangeName}")
    private lateinit var exchangeName: String;

    private val logger = LoggerFactory.getLogger(DefaultResultsConsumer::class.java)

    override fun consume(result: ARXResult, data: Data, request: AnonymizationRequest) {
        this.logger.info("Consuming results of the request [{}]", request.id)
        val optimum = result.globalOptimum ?: return

        val output = result.output
        val dataRows = arrayListOf<Array<String>>()
        for (row in output.iterator()) {
            dataRows.add(row)
        }

        val dataset = MongoDbDatasetWriter(this.mongoDatabase, "outputDatasets").write(Dataset(data = dataRows))
        logger.info("Saved optimal results to MongoDb with id [{}]", dataset.id)

        val solutionCandidates = arrayListOf<SolutionCandidate>()
        for (level in result.lattice.levels) {
            for (node in level) {
                val solutionCandidate = SolutionCandidate(
                    generalizations = this.extractAttributeGeneralization(node),
                    isAnonymized = node.anonymity.equals(ARXLattice.Anonymity.ANONYMOUS),
                    isOptimal = node.equals(optimum),
                    informationLost = ("${node.highestScore}".toDouble() * 1e8).roundToInt() * 1e-8
                )
                solutionCandidates.add(solutionCandidate)
            }
        }

        val anonymizationResults = AnonymizationResults(
            requestId = request.id.toString(),
            optimalSolutionDatasetId = dataset.id.toString(),
            maxGeneralizations = this.extractMaxGeneralizationLevels(data),
            solutionCandidates = solutionCandidates.toTypedArray()
        )

        this.saveAnonymizationResults(anonymizationResults)
    }

    private fun extractAttributeGeneralization(node: ARXLattice.ARXNode): Array<AttributeGeneralizationInfo> {
        val generalization = arrayListOf<AttributeGeneralizationInfo>()
        for (attribute in node.quasiIdentifyingAttributes) {
            generalization.add(AttributeGeneralizationInfo(attribute, node.getGeneralization(attribute)))
        }
        return generalization.toTypedArray()
    }

    private fun extractMaxGeneralizationLevels(data: Data): Array<AttributeGeneralizationInfo> {
        val generalization = arrayListOf<AttributeGeneralizationInfo>()
        for (attribute in data.definition.quasiIdentifyingAttributes) {
            if (data.definition.isHierarchyAvailable(attribute)) {
                generalization.add(
                    AttributeGeneralizationInfo(
                        attribute,
                        (data.definition.getHierarchy(attribute)[0].size - 1)
                    )
                )
            }
        }
        return generalization.toTypedArray()
    }

    private fun saveAnonymizationResults(anonymizationResults: AnonymizationResults): Unit {
        val collection = this.mongoDatabase.getCollection<AnonymizationResults>("solutions")
        collection.insertOne(anonymizationResults)

        this.rabbitTemplate.convertAndSend(this.exchangeName, ConsumerConfiguration.RESULTS_QUEUE_NAME, anonymizationResults)
        this.logger.info("Published solutions to RabbitMQ [{}]", anonymizationResults.id.toString())
    }
}
