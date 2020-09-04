package info.hieule.arx_automation.shared.models

data class SolutionCandidate(
    val generalizations: Array<AttributeGeneralizationInfo>,
    val isAnonymized: Boolean,
    val isOptimal: Boolean,
    val informationLost: Double?
)