package info.hieule.arx_automation.app.springboot

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.context.ConfigurableApplicationContext

class ApplicationCommandLineRunner() : CommandLineRunner {
    @Value("\${tutorial.client.duration:0}")
    private var duration: Long? = null

    @Autowired
    private lateinit var ctx: ConfigurableApplicationContext

    override fun run(vararg args: String?) {
        if (this.duration === null) {
            println("Duration must not be null")
            return
        }

        println("Ready ... running for ${this.duration} ms")
        Thread.sleep(this.duration!!)
        this.ctx.close()
        println("Exit!")
    }
}
