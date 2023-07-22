import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.21"
    kotlin("plugin.spring") version "1.8.21"
    id("org.springframework.boot") version "3.1.0"
    id("io.spring.dependency-management") version "1.1.0"
    application
}

group = "com.fkh.bookapp"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("com.fkh.bookapp.BookAppKt")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-graphql")
    implementation("io.arrow-kt:arrow-core:1.1.2")

    testImplementation("io.mockk:mockk:1.13.3")
    testImplementation("com.ninja-squad:springmockk:3.1.1")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.8.2")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.graphql:spring-graphql-test")
    testImplementation("org.springframework.graphql:spring-graphql")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}
