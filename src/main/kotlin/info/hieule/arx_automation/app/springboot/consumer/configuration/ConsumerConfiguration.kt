package info.hieule.arx_automation.app.springboot.consumer.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import info.hieule.arx_automation.app.springboot.consumer.AnonymizationReceiver
import org.springframework.amqp.core.*
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Profile("worker")
@Configuration
class ConsumerConfiguration {

    public companion object {
        const val REQUESTS_QUEUE_NAME = "requests"
        const val RESULTS_QUEUE_NAME = "results"
        const val PUBLICATIONS_QUEUE_NAME = "publications"
    }

    @Value("\${app.rabbitmq.exchangeName}")
    private lateinit var exchangeName: String;

    @Autowired
    private lateinit var objectMapper: ObjectMapper;

    @Bean
    public fun requestsQueue(): Queue {
        return QueueBuilder.durable(REQUESTS_QUEUE_NAME)
                .build();
    }

    @Bean
    public fun resultsQueue(): Queue {
        return QueueBuilder.durable(RESULTS_QUEUE_NAME)
                .build();
    }

    @Bean
    public fun publicationsQueue(): Queue {
        return QueueBuilder.durable(PUBLICATIONS_QUEUE_NAME)
                .build();
    }

    @Bean
    public fun exchange(): TopicExchange {
        return TopicExchange(this.exchangeName);
    }

    @Bean
    public fun requestsBinding(exchange: TopicExchange, requestsQueue: Queue): Binding {
        return BindingBuilder.bind(requestsQueue).to(exchange).with(REQUESTS_QUEUE_NAME);
    }

    @Bean
    public fun resultsBinding(exchange: TopicExchange, resultsQueue: Queue): Binding {
        return BindingBuilder.bind(resultsQueue).to(exchange).with(RESULTS_QUEUE_NAME);
    }

    @Bean
    public fun publicationsBinding(exchange: TopicExchange, publicationsQueue: Queue): Binding {
        return BindingBuilder.bind(publicationsQueue).to(exchange).with(PUBLICATIONS_QUEUE_NAME);
    }

    @Bean
    public fun rabbitTemplate(connectionFactory: ConnectionFactory): RabbitTemplate {
        val rabbitTemplate = RabbitTemplate(connectionFactory);
        rabbitTemplate.messageConverter = jacksonMessageConverter()
        return rabbitTemplate
    }

    @Bean
    public fun jacksonMessageConverter(): Jackson2JsonMessageConverter {
        return Jackson2JsonMessageConverter(this.objectMapper)
    }
}
