package info.hieule.arx_automation.app.adapter.dataset_reader

import com.mongodb.client.MongoDatabase
import info.hieule.arx_automation.ports.DatasetReader
import info.hieule.arx_automation.shared.models.Dataset
import org.litote.kmongo.findOneById
import org.litote.kmongo.getCollection
import org.litote.kmongo.id.WrappedObjectId

class MongoDbDatasetReader(
    private val database: MongoDatabase,
    private val collectionName: String,
    private val datasetId: String
) : DatasetReader {

    override fun read(): Dataset {
        val collection = this.database.getCollection<Dataset>(this.collectionName)

        return collection.findOneById(WrappedObjectId<Dataset>(datasetId))
            ?: throw RuntimeException("Cannot find dataset with id [$datasetId] in Mongo collection [$collectionName]");
    }
}