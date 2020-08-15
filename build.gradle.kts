import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    kotlin("jvm") version "1.3.72"
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
    implementation(group = "commons-cli", name = "commons-cli", version = "1.4")
    implementation(group = "org.litote.kmongo", name = "kmongo", version = "4.1.0")
    implementation(group = "org.litote.kmongo", name = "kmongo-id", version = "4.1.0")
    implementation(kotlin("stdlib-jdk8"))
}

tasks.create<JavaExec>("demoPublisher") {
    main = "info.hieule.arx_automation.app.DemoPublisherKt"
    classpath = sourceSets["main"].runtimeClasspath
}

tasks.create<JavaExec>("demoConsumer") {
    main = "info.hieule.arx_automation.app.DemoConsumerKt"
    classpath = sourceSets["main"].runtimeClasspath
}

tasks.create<JavaExec>("frontEnd") {
    main = "info.hieule.arx_automation.app.front_end.ApplicationKt"
    classpath = sourceSets["main"].runtimeClasspath
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
