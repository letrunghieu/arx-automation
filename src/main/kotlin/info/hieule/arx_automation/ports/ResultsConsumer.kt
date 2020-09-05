package info.hieule.arx_automation.ports

import info.hieule.arx_automation.shared.models.AnonymizationRequest
import org.deidentifier.arx.ARXResult
import org.deidentifier.arx.Data

interface ResultsConsumer {
    fun consume(result: ARXResult, data: Data, request: AnonymizationRequest)
}
