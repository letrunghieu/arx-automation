package info.hieule.arx_automation.app.adapter.dataset_writer

import com.mongodb.client.MongoDatabase
import info.hieule.arx_automation.ports.DatasetWriter
import info.hieule.arx_automation.shared.models.Dataset
import org.litote.kmongo.getCollection

class MongoDbDatasetWriter(
    private val database: MongoDatabase,
    private val collectionName: String
) : DatasetWriter {
    override fun write(dataset: Dataset): Dataset {
        val collection = this.database.getCollection<Dataset>(this.collectionName)

        collection.insertOne(dataset)

        return dataset;
    }
}
