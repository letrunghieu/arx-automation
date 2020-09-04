package info.hieule.arx_automation.app.springboot

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class Application {
    @Bean
    public fun commandLineRunner(): CommandLineRunner {
        return ApplicationCommandLineRunner();
    }
}

fun main(args: Array<String>) {
    val ctx = runApplication<Application>(*args)
}
