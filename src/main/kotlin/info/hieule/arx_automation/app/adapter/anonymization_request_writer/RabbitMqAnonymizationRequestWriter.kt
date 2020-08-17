package info.hieule.arx_automation.app.adapter.anonymization_request_writer

import com.fasterxml.jackson.databind.ObjectMapper
import com.rabbitmq.client.Connection
import info.hieule.arx_automation.ports.AnonymizationRequestWriter
import info.hieule.arx_automation.shared.models.AnonymizationRequest

class RabbitMqAnonymizationRequestWriter(
    private val connection: Connection,
    private val objectMapper: ObjectMapper
) : AnonymizationRequestWriter {
    private companion object {
        const val QUEUE_NAME = "anonymization"
    }

    override fun write(request: AnonymizationRequest): AnonymizationRequest {
        this.connection.createChannel().use { channel ->
            channel.queueDeclare(QUEUE_NAME, false, false, false, null)
            channel.basicPublish("", QUEUE_NAME, null, objectMapper.writeValueAsBytes(request))
            println(" [x] Sent to [${QUEUE_NAME}] queue")
        }

        return request
    }
}