package info.hieule.arx_automation.app.springboot.consumer.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.litote.kmongo.id.jackson.IdJacksonModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ObjectMapperConfiguration {

    @Bean
    public fun objectMapper(): ObjectMapper {
        return jacksonObjectMapper().registerModule(IdJacksonModule());
    }
}
