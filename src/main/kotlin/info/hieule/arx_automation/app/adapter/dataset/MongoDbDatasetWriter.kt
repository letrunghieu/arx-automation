package info.hieule.arx_automation.app.adapter.dataset

import com.mongodb.client.MongoDatabase
import info.hieule.arx_automation.ports.dataset.DatasetWriter
import info.hieule.arx_automation.shared.models.Dataset
import org.litote.kmongo.getCollection

class MongoDbDatasetWriter(
    private val database: MongoDatabase,
    private val collectionName: String
) : DatasetWriter {
    override fun write(dataset: Dataset): Dataset {
        val collection = this.database.getCollection<Dataset>("originalDatasets")

        val result = collection.insertOne(dataset)
        return dataset;
    }
}
