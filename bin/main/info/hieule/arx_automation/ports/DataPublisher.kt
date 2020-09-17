package info.hieule.arx_automation.ports

import info.hieule.arx_automation.shared.models.PublicationRequest
import info.hieule.arx_automation.shared.models.PublicationResult

interface DataPublisher {
    public fun publish(publicationRequest: PublicationRequest): PublicationResult
}
