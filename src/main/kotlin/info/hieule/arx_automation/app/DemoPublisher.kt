@file:JvmName("DemoPublisher")

package info.hieule.arx_automation.app

import com.rabbitmq.client.ConnectionFactory

private const val QUEUE_NAME = "dataset"

fun main(args: Array<String>) {
    val connectionFactory = ConnectionFactory()
    connectionFactory.host = "localhost"
    connectionFactory.connectionTimeout = 3000
    connectionFactory.newConnection().use { connection ->
        connection.createChannel().use { channel ->
            channel.queueDeclare(QUEUE_NAME, false, false, false, null)
            val message = "Hello World!"
            channel.basicPublish("", QUEUE_NAME, null, message.toByteArray())
            println(" [x] Sent '$message'")
        }
    }
}
