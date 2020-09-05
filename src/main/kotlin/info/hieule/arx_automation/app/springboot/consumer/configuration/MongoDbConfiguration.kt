package info.hieule.arx_automation.app.springboot.consumer.configuration

import com.mongodb.client.MongoDatabase
import org.litote.kmongo.KMongo
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MongoDbConfiguration {
    @Value("\${app.mongodb.hostname}")
    private lateinit var hostname: String;

    @Value("\${app.mongodb.username}")
    private lateinit var username: String;

    @Value("\${app.mongodb.password}")
    private lateinit var password: String;

    @Value("\${app.mongodb.database}")
    private lateinit var database: String;

    @Bean
    public fun anonymizationDatabase(): MongoDatabase {
        val mongo = KMongo.createClient("mongodb://${this.username}:${this.password}@${this.hostname}/?ssl=false")
        return mongo.getDatabase(this.database);
    }
}
