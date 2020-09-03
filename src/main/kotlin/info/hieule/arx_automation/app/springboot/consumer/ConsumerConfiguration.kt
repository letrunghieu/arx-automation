package info.hieule.arx_automation.app.springboot.consumer

import org.springframework.amqp.core.AnonymousQueue
import org.springframework.amqp.core.FanoutExchange
import org.springframework.amqp.core.Queue
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Profile("worker")
@Configuration
class ConsumerConfiguration {

    @Value("\${app.rabbitmq.exchangeName}")
    private lateinit var exchangeName: String;

    @Bean
    public fun queue(): Queue {
        return AnonymousQueue();
    }

    @Bean
    public fun exchange(): FanoutExchange
    {
        return FanoutExchange(this.exchangeName);
    }
}