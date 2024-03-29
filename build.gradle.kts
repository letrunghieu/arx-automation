import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
//    application
    id("org.springframework.boot") version "2.3.3.RELEASE"
    id("io.spring.dependency-management") version "1.0.10.RELEASE"
    kotlin("jvm") version "1.3.72"
    kotlin("plugin.spring") version "1.3.72"
}

group = "info.hieule"
version = "1.0-SNAPSHOT"

//application {
//    mainClass.set("info.hieule.arx_automation.app.AnonymizationEngineKt")
//}

springBoot {
    mainClassName = "info.hieule.arx_automation.app.springboot.ApplicationKt"
}

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
    implementation(group = "org.apache.commons", name = "commons-csv", version = "1.8")
    implementation(group = "org.springframework.boot", name = "spring-boot-starter-amqp", version = "2.3.3.RELEASE")
    implementation(group = "com.github.kittinunf.fuel", name = "fuel", version = "2.2.3")
    implementation(group = "com.github.kittinunf.result", name = "result", version = "3.1.0")
    implementation(kotlin("stdlib-jdk8"))
}

tasks.register<JavaExec>("demoPublisher") {
    main = "info.hieule.arx_automation.app.DemoPublisherKt"
    classpath = sourceSets["main"].runtimeClasspath
}

tasks.register<JavaExec>("demoConsumer") {
    main = "info.hieule.arx_automation.app.DemoConsumerKt"
    classpath = sourceSets["main"].runtimeClasspath
}

tasks.register<JavaExec>("frontEnd") {
    main = "info.hieule.arx_automation.app.ApplicationKt"
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
