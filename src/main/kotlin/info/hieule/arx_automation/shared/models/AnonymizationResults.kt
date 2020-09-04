package info.hieule.arx_automation.shared.models

import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id

data class AnonymizationResults(
    @BsonId val id: Id<AnonymizationResults>? = null,
    val requestId: String,
    val optimalSolutionDatasetId: String,
    val maxGeneralizations: Array<AttributeGeneralizationInfo>,
    val solutionCandidates: Array<SolutionCandidate>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AnonymizationResults

        if (id != other.id) return false
        if (requestId != other.requestId) return false
        if (optimalSolutionDatasetId != other.optimalSolutionDatasetId) return false
        if (!maxGeneralizations.contentEquals(other.maxGeneralizations)) return false
        if (!solutionCandidates.contentEquals(other.solutionCandidates)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + requestId.hashCode()
        result = 31 * result + optimalSolutionDatasetId.hashCode()
        result = 31 * result + maxGeneralizations.contentHashCode()
        result = 31 * result + solutionCandidates.contentHashCode()
        return result
    }
}