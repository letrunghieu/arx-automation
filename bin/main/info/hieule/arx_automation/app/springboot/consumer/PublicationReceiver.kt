package info.hieule.arx_automation.app.springboot.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import com.mongodb.client.MongoDatabase
import info.hieule.arx_automation.app.adapter.dataset_reader.MongoDbDatasetReader
import info.hieule.arx_automation.app.springboot.adapter.data_publisher.CkanDataPublisher
import info.hieule.arx_automation.app.springboot.consumer.configuration.CkanConnectorConfiguration
import info.hieule.arx_automation.app.springboot.consumer.configuration.ConsumerConfiguration
import info.hieule.arx_automation.shared.models.PublicationRequest
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class PublicationReceiver(
    private val objectMapper: ObjectMapper,
    private val anonymizationDatabase: MongoDatabase,
    private val rabbitTemplate: RabbitTemplate,
    private val ckanConnectorConfiguration: CkanConnectorConfiguration
) {
    @Value("\${app.rabbitmq.exchangeName}")
    private lateinit var exchangeName: String;

    private val logger = LoggerFactory.getLogger(PublicationReceiver::class.java)

    @RabbitListener(queues = ["#{publicationsQueue.name}"])
    public fun receive(message: Message) {
        this.logger.info("Start processing a publication request!")
        val publicationRequest: PublicationRequest = this.objectMapper.readerFor(PublicationRequest::class.java)
            .readValue(message.body)

        val datasetReader = MongoDbDatasetReader(
            this.anonymizationDatabase,
            "outputDatasets",
            publicationRequest.datasetId
        )

        val publicationResult = CkanDataPublisher(
            objectMapper,
            datasetReader,
            ckanConnectorConfiguration
        ).publish(publicationRequest)

        this.logger.info("A new dataset is published at [{}]", publicationResult.datasetUrl)

        this.rabbitTemplate.convertAndSend(this.exchangeName, ConsumerConfiguration.PUBLICATION_RESULTS_QUEUE_NAME, publicationResult)
    }
}
