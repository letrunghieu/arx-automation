package info.hieule.arx_automation.shared.models

import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id

data class PublicationRequest(
        @BsonId val id: Id<PublicationRequest>? = null,
        val title: String,
        val datasetId: String,
        val description: String,
        val requestId: String
)
