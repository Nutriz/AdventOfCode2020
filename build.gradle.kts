import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.20"
    application
}
group = "fr.jgully"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}
dependencies {
    testImplementation(kotlin("test-junit5"))
}
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "13"
}
application {
    mainClassName = "MainKt"
}