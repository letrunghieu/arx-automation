package info.hieule.arx_automation.app.adapter.results_consumer

import com.mongodb.client.MongoDatabase
import info.hieule.arx_automation.app.adapter.dataset_writer.MongoDbDatasetWriter
import info.hieule.arx_automation.ports.ResultsConsumer
import info.hieule.arx_automation.shared.models.AnonymizationRequest
import info.hieule.arx_automation.shared.models.Dataset
import org.deidentifier.arx.ARXResult
import org.deidentifier.arx.Data

class MongoDbResultsConsumer(
    private val mongoDatabase: MongoDatabase
) : ResultsConsumer {
    override fun consume(result: ARXResult, data: Data, request: AnonymizationRequest) {
        val optimum = result.globalOptimum ?: return

        val output = result.output
        val dataRows = arrayListOf<Array<String>>()
        for (row in output.iterator()) {
            dataRows.add(row)
        }

        val dataset = MongoDbDatasetWriter(this.mongoDatabase, "outputDatasets").write(Dataset(data = dataRows))
        println("The output is written to dataset: ${dataset.id.toString()}.")
    }
}