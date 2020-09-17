package info.hieule.arx_automation.ports

import info.hieule.arx_automation.shared.models.AnonymizationRequest

interface AnonymizationRequestWriter {
    fun write(request: AnonymizationRequest): AnonymizationRequest
}