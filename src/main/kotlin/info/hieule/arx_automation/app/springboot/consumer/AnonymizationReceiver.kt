package info.hieule.arx_automation.app.springboot.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import info.hieule.arx_automation.app.adapter.data_provider.AnonymizationRequestDataProvider
import info.hieule.arx_automation.app.adapter.results_consumer.MongoDbResultsConsumer
import info.hieule.arx_automation.shared.models.AnonymizationRequest
import info.hieule.arx_automation.use_cases.AnonymizingData
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class AnonymizationReceiver() {

    @Autowired
    private lateinit var objectMapper: ObjectMapper;

    @RabbitListener(queues = ["#{requestsQueue.name}"])
    public fun receive(message: Message) {
        println("Received: ${message.messageProperties.messageId}");
        val anonymizationRequest: AnonymizationRequest = objectMapper.readerFor(AnonymizationRequest::class.java)
                .readValue(message.body)
        println(this.objectMapper.writeValueAsString(anonymizationRequest));

        val dataProvider = AnonymizationRequestDataProvider(anonymizationRequest, anonymizationDb)
        val resultsConsumer = MongoDbResultsConsumer(anonymizationDb)

        val useCase = AnonymizingData(dataProvider, resultsConsumer)
        useCase.execute(0)
    }
}
