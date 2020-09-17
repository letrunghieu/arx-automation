package info.hieule.arx_automation.shared.models

import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id

data class AnonymizationRequest(
        @BsonId val id: Id<AnonymizationRequest>? = null,
        val title: String = "",
        val datasetId: String,
        val hierarchies: Array<HierarchyAttributeInfo> = arrayOf(),
        val sensitiveAttributes: Array<String> = arrayOf(),
        val identifierAttributes: Array<String> = arrayOf(),
        val anonymizeModelConfigs: Map<String, Any> = mapOf()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AnonymizationRequest

        if (id != other.id) return false
        if (datasetId != other.datasetId) return false
        if (!hierarchies.contentEquals(other.hierarchies)) return false
        if (!sensitiveAttributes.contentEquals(other.sensitiveAttributes)) return false
        if (!identifierAttributes.contentEquals(other.identifierAttributes)) return false
        if (anonymizeModelConfigs != other.anonymizeModelConfigs) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + datasetId.hashCode()
        result = 31 * result + hierarchies.contentHashCode()
        result = 31 * result + sensitiveAttributes.contentHashCode()
        result = 31 * result + identifierAttributes.contentHashCode()
        result = 31 * result + anonymizeModelConfigs.hashCode()
        return result
    }
}
