package info.hieule.arx_automation.app.springboot

import org.springframework.stereotype.Component
import java.util.concurrent.CountDownLatch

@Component
public class Receiver {
    private val latch: CountDownLatch = CountDownLatch(1)

    public fun receiveMessage(message: String): Unit {
        println("Receive <${message}>")
        latch.countDown()
    }

    public fun getLatch(): CountDownLatch {
        return this.latch
    }
}