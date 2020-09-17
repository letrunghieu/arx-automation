package info.hieule.arx_automation.app.springboot.consumer.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "app.ckan")
class CkanConnectorConfiguration {
    lateinit var apiKey: String
    lateinit var hostname: String
    lateinit var organization: String
    lateinit var defaultLicense: String
}
