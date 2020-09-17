package info.hieule.arx_automation.shared.models

import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id
import org.litote.kmongo.newId

data class Dataset(
    @BsonId val id: Id<Dataset>? = null,
    val data: List<Array<String>>
)
