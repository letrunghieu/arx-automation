package info.hieule.arx_automation.app.springboot.adapter.data_publisher

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.kittinunf.fuel.core.FileDataPart
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.core.requests.upload
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result;
import info.hieule.arx_automation.app.springboot.consumer.configuration.CkanConnectorConfiguration
import info.hieule.arx_automation.ports.DataPublisher
import info.hieule.arx_automation.ports.DatasetReader
import info.hieule.arx_automation.shared.models.Dataset
import info.hieule.arx_automation.shared.models.PublicationRequest
import info.hieule.arx_automation.shared.models.PublicationResult
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileWriter
import java.util.*

class CkanDataPublisher(
        private val objectMapper: ObjectMapper,
        private val datasetReader: DatasetReader,
        private val ckanConnectorConfiguration: CkanConnectorConfiguration
) : DataPublisher {
    private val logger = LoggerFactory.getLogger(CkanDataPublisher::class.java)

    override fun publish(publicationRequest: PublicationRequest): PublicationResult {
        val dataset = this.datasetReader.read()

        val datasetCsvFile = this.createTmpCsv(dataset)
        val datasetName = this.generatePackageName()

        this.createPackage(datasetName, publicationRequest)
        this.logger.info("Created dataset [{}] on CKAN.", datasetName)
        this.uploadResource(datasetName, datasetCsvFile)

        return PublicationResult(
                publicationRequest.requestId,
                "${this.ckanConnectorConfiguration.hostname}/dataset/${datasetName}"
        )
    }

    private fun createTmpCsv(dataset: Dataset): File {
        val file = createTempFile(suffix = ".csv")

        this.logger.info("A temporary dataset is created at [{}]", file.absolutePath)
        val fileWriter = FileWriter(file)
        for (record in dataset.data) {
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
        val (request, response, result) = "${this.ckanConnectorConfiguration.hostname}/api/action/package_create"
                .httpPost()
                .header("Authorization", this.ckanConnectorConfiguration.apiKey)
                .jsonBody("{\n" +
                        "    \"name\": \"${packageName}\",\n" +
                        "    \"title\": \"${publicationRequest.title}\",\n" +
                        "    \"notes\": \"${publicationRequest.description}\",\n" +
                        "    \"license_id\": \"${this.ckanConnectorConfiguration.defaultLicense}\",\n" +
                        "    \"owner_org\": \"${this.ckanConnectorConfiguration.organization}\"\n" +
                        "}")
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
