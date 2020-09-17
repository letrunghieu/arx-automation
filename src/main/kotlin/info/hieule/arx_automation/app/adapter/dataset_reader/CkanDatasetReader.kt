package info.hieule.arx_automation.app.adapter.dataset_reader

import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import info.hieule.arx_automation.ports.DatasetReader
import info.hieule.arx_automation.shared.models.Dataset
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileWriter

class CkanDatasetReader(
    private val resourceUrl: String
) : DatasetReader {
    private val logger = LoggerFactory.getLogger(CkanDatasetReader::class.java)

    override fun read(): Dataset {
        val localFile = this.downloadFromCkanAndSaveToLocal()

        return CsvDatasetReader(localFile.toPath()).read()
    }

    private fun downloadFromCkanAndSaveToLocal(): File {
        val localFile = createTempFile(prefix = "ckan-", suffix = ".csv")
        val fileWriter = FileWriter(localFile)

        this.logger.info("Downloading dataset from [{}]", this.resourceUrl)
        val (request, response, result) = this.resourceUrl
            .httpGet()
            .responseString()

        when (result) {
            is Result.Success -> {
                fileWriter.append(response.body().asString("text/plain"))
                this.logger.info("Saved to [{}]", localFile.absolutePath)
            }

            is Result.Failure -> {
                throw RuntimeException("Failed to create package")
            }
        }

        fileWriter.flush()
        fileWriter.close()

        return localFile
    }
}
