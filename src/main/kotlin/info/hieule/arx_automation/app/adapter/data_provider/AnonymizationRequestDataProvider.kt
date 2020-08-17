package info.hieule.arx_automation.app.adapter.data_provider

import com.mongodb.client.MongoDatabase
import info.hieule.arx_automation.app.adapter.dataset_reader.MongoDbDatasetReader
import info.hieule.arx_automation.ports.DataProvider
import info.hieule.arx_automation.shared.models.AnonymizationRequest
import org.deidentifier.arx.AttributeType
import org.deidentifier.arx.Data

class AnonymizationRequestDataProvider(
    private val request: AnonymizationRequest,
    private val mongoDatabase: MongoDatabase
) : DataProvider {
    private companion object {
        const val ORIGINAL_DATASET_COLLECTION = "originalDatasets"
        const val HIERARCHIES_COLLECTION = "hierarchies"
    }

    override fun getData(): Data {
        val dataset = MongoDbDatasetReader(this.mongoDatabase, ORIGINAL_DATASET_COLLECTION, this.request.datasetId).read()
        val data = Data.create(dataset.data)

        val dataAttributes = dataset.data.first()

        val quasiIdentifierAttributes = arrayListOf<String>()
        for (hierarchy in this.request.hierarchies) {
            quasiIdentifierAttributes.add(hierarchy.attribute)

            val hierarchyDataset = MongoDbDatasetReader(this.mongoDatabase, HIERARCHIES_COLLECTION, hierarchy.datasetId).read()
            data.definition.setAttributeType(hierarchy.attribute, AttributeType.Hierarchy.create(hierarchyDataset.data))
        }

        val sensitiveAttributes = this.request.sensitiveAttributes
        for (attribute in this.request.sensitiveAttributes) {
            data.definition.setAttributeType(attribute, AttributeType.SENSITIVE_ATTRIBUTE)
        }

        val identifierAttribute = this.request.identifierAttributes
        for (attribute in this.request.identifierAttributes) {
            data.definition.setAttributeType(attribute, AttributeType.IDENTIFYING_ATTRIBUTE)
        }

//        val insensitiveAttributes = dataAttributes.toList()
//            .minus(quasiIdentifierAttributes)
//            .minus(sensitiveAttributes)
//            .minus(identifierAttribute)
//        for (attribute in insensitiveAttributes) {
//            data.definition.setAttributeType(attribute, AttributeType.INSENSITIVE_ATTRIBUTE)
//        }

        return data
    }
}