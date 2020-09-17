package info.hieule.arx_automation.app.springboot.adapter.results_consumer

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.kittinunf.fuel.core.FileDataPart
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.core.requests.upload
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import info.hieule.arx_automation.app.springboot.consumer.configuration.CkanConnectorConfiguration
import info.hieule.arx_automation.app.springboot.consumer.configuration.ConsumerConfiguration
import info.hieule.arx_automation.ports.ResultsConsumer
import info.hieule.arx_automation.shared.models.AnonymizationRequest
import info.hieule.arx_automation.shared.models.PublicationRequest
import info.hieule.arx_automation.shared.models.PublicationResult
import org.deidentifier.arx.ARXResult
import org.deidentifier.arx.Data
import org.deidentifier.arx.DataHandle
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.File
import java.io.FileWriter
import java.util.*
import kotlin.math.roundToInt

@Component
class CkanResultsConsumer(
    private val objectMapper: ObjectMapper,
    private val rabbitTemplate: RabbitTemplate,
    private val ckanConnectorConfiguration: CkanConnectorConfiguration
) : ResultsConsumer {
    @Value("\${app.rabbitmq.exchangeName}")
    private lateinit var exchangeName: String;

    private val logger = LoggerFactory.getLogger(CkanResultsConsumer::class.java)

    override fun consume(result: ARXResult, data: Data, request: AnonymizationRequest) {
        this.logger.info("Consuming results of the request [{}]", request.id)

        val output = result.output
        val datasetCsvFile = this.createTmpCsv(output)
        val datasetName = this.generatePackageName()

        val informationLoss = ("${result.globalOptimum.highestScore}".toDouble() * 1e8).roundToInt() * 1e-8

        val publicationRequest = PublicationRequest(
            title = request.title,
            description = "Information loss: ${informationLoss}\n\n" +
                "Anonymization model: ${this.objectMapper.writeValueAsString(request.anonymizeModelConfigs)}\n",
            requestId = request.id.toString(),
            datasetId = request.datasetId
        )

        this.createPackage(datasetName, publicationRequest)
        this.logger.info("Created dataset [{}] on CKAN.", datasetName)
        this.uploadResource(datasetName, datasetCsvFile)

        val publicationResult = PublicationResult(
            request.id.toString(),
            "${this.ckanConnectorConfiguration.hostname}/dataset/${datasetName}"
        )

        this.logger.info("A new dataset is published at [{}]", publicationResult.datasetUrl)

        this.rabbitTemplate.convertAndSend(this.exchangeName, ConsumerConfiguration.RESULTS_QUEUE_NAME, publicationResult)
    }

    private fun createTmpCsv(dataHandle: DataHandle): File {
        val file = createTempFile(suffix = ".csv")

        this.logger.info("A temporary dataset is created at [{}]", file.absolutePath)
        val fileWriter = FileWriter(file)
        for (record in dataHandle) {
            fileWriter.append(record.joinToString(","))
            fileWriter.append("\n")
        }
        fileWriter.flush()
        fileWriter.close()

        return file
    }

    private fun generatePackageName(): String {
        return UUID.randomUUID().toString()
    }

    private fun createPackage(packageName: String, publicationRequest: PublicationRequest) {
        val bodyMap: Map<String, String> = mapOf(
            "name" to packageName,
            "title" to publicationRequest.title,
            "notes" to publicationRequest.description,
            "license_id" to this.ckanConnectorConfiguration.defaultLicense,
            "owner_org" to this.ckanConnectorConfiguration.organization
        )

        val (request, response, result) = "${this.ckanConnectorConfiguration.hostname}/api/action/package_create"
            .httpPost()
            .header("Authorization", this.ckanConnectorConfiguration.apiKey)
            .jsonBody(this.objectMapper.writeValueAsString(bodyMap))
            .responseString()

        when (result) {
            is Result.Success -> {
                return
            }

            is Result.Failure -> {
                throw RuntimeException("Failed to create package")
            }
        }
    }

    private fun uploadResource(packageName: String, csvFile: File) {
        val (request, response, result) = "${this.ckanConnectorConfiguration.hostname}/api/action/resource_create"
            .httpPost(parameters = listOf("package_id" to packageName))
            .header("Authorization", this.ckanConnectorConfiguration.apiKey)
            .upload()
            .add(
                FileDataPart(csvFile, name = "upload")
            )
            .responseString()

        when (result) {
            is Result.Success -> {
                return
            }

            is Result.Failure -> {
                throw RuntimeException("Failed to create package")
            }
        }
    }
}
