plugins {
    application
}

group = "info.hieule"
version = "1.0-SNAPSHOT"


repositories {
    mavenCentral()
}

dependencies {
    testCompileOnly(group = "junit", name = "junit", version = "4.12")
    implementation(files("libs/gradle/libarx-3.8.0.jar"))
    implementation(group = "com.rabbitmq", name = "amqp-client", version = "5.9.0")
}

tasks.create<JavaExec>("demoPublisher") {
    main = "info.hieule.arx_automation.app.DemoPublisher"
    classpath = sourceSets["main"].runtimeClasspath
}
