package info.hieule.arx_automation.app.adapter.anonymization_request_writer

import com.mongodb.client.MongoDatabase
import info.hieule.arx_automation.ports.AnonymizationRequestWriter
import info.hieule.arx_automation.shared.models.AnonymizationRequest
import org.litote.kmongo.getCollection

class MongoDbAnonymizationRequestWriter(private val database: MongoDatabase) : AnonymizationRequestWriter {
    private companion object {
        const val COLLECTION_NAME = "requests"
    }

    override fun write(request: AnonymizationRequest): AnonymizationRequest {
        val collection = this.database.getCollection<AnonymizationRequest>(COLLECTION_NAME)

        collection.insertOne(request)

        return request
    }
}