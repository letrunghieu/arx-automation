package info.hieule.arx_automation.app

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.rabbitmq.client.ConnectionFactory
import info.hieule.arx_automation.app.adapter.data_provider.AnonymizationRequestDataProvider
import info.hieule.arx_automation.app.adapter.results_consumer.MongoDbResultsConsumer
import info.hieule.arx_automation.app.demo.adapters.ConsoleResultsConsumer
import info.hieule.arx_automation.shared.models.AnonymizationRequest
import info.hieule.arx_automation.use_cases.AnonymizingData
import org.litote.kmongo.KMongo
import org.litote.kmongo.id.jackson.IdJacksonModule


private const val QUEUE_NAME = "anonymization"

fun main(args: Array<String>) {
    val objectMapper = jacksonObjectMapper().registerModule(IdJacksonModule())

    val mongo = KMongo.createClient("mongodb://root:secret@localhost/?ssl=false")
    val anonymizationDb = mongo.getDatabase("anonymization");

    val connectionFactory = ConnectionFactory()
    connectionFactory.host = "localhost"
    connectionFactory.connectionTimeout = 3000
    connectionFactory.newConnection().use { connection ->
        connection.createChannel().use { channel ->
            channel.queueDeclare(QUEUE_NAME, false, false, false, null)
            println("Waiting for new requests ...")

            channel.basicConsume(QUEUE_NAME, true, { _, message ->
                val anonymizationRequest: AnonymizationRequest = objectMapper.reader().forType(AnonymizationRequest::class.java).readValue(message.body)
                println("Received new requests with id: ${anonymizationRequest.id.toString()}")

                val dataProvider = AnonymizationRequestDataProvider(anonymizationRequest, anonymizationDb)
                val resultsConsumer = MongoDbResultsConsumer(anonymizationDb)

                val useCase = AnonymizingData(dataProvider, resultsConsumer)
                useCase.execute(anonymizationRequest)
            }, { _ -> })
        }
    }
}