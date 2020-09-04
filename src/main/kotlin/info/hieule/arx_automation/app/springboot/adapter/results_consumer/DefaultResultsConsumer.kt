package info.hieule.arx_automation.app.springboot.adapter.results_consumer

import com.mongodb.client.MongoDatabase
import info.hieule.arx_automation.app.adapter.dataset_writer.MongoDbDatasetWriter
import info.hieule.arx_automation.ports.ResultsConsumer
import info.hieule.arx_automation.shared.models.Dataset
import org.deidentifier.arx.ARXResult
import org.deidentifier.arx.Data
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Component

@Component
class DefaultResultsConsumer(
        private val mongoDatabase: MongoDatabase,
        private val rabbitTemplate: RabbitTemplate
): ResultsConsumer {

    private val logger = LoggerFactory.getLogger(DefaultResultsConsumer::class.java)

    override fun consume(result: ARXResult, data: Data) {
        val optimum = result.globalOptimum ?: return

        val output = result.output
        val dataRows = arrayListOf<Array<String>>()
        for (row in output.iterator()) {
            dataRows.add(row)
        }

        val dataset = MongoDbDatasetWriter(this.mongoDatabase, "outputDatasets").write(Dataset(data = dataRows))
    }
}
