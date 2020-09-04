package info.hieule.arx_automation.app.springboot.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import com.mongodb.client.MongoDatabase
import info.hieule.arx_automation.app.adapter.data_provider.AnonymizationRequestDataProvider
import info.hieule.arx_automation.app.adapter.results_consumer.MongoDbResultsConsumer
import info.hieule.arx_automation.ports.ResultsConsumer
import info.hieule.arx_automation.shared.models.AnonymizationRequest
import info.hieule.arx_automation.use_cases.AnonymizingData
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class AnonymizationReceiver(
        private val objectMapper: ObjectMapper,
        private val anonymizationDatabase: MongoDatabase,
        private val defaultResultsConsumer: ResultsConsumer
) {

//    @Autowired
//    private lateinit var objectMapper: ObjectMapper
//
//    @Autowired
//    private lateinit var anonymizationDatabase: MongoDatabase

    private val logger = LoggerFactory.getLogger(AnonymizationReceiver::class.java)

    @RabbitListener(queues = ["#{requestsQueue.name}"])
    public fun receive(message: Message) {
        this.logger.info("Start processing a new request!")
        val anonymizationRequest: AnonymizationRequest = objectMapper.readerFor(AnonymizationRequest::class.java)
                .readValue(message.body)
        this.logger.info("Message: {}", this.objectMapper.writeValueAsString(anonymizationRequest))

        val dataProvider = AnonymizationRequestDataProvider(anonymizationRequest, this.anonymizationDatabase)
        val useCase = AnonymizingData(dataProvider, this.defaultResultsConsumer)
        useCase.execute(0)

        this.logger.info("Finish processing a request.")
    }
}
