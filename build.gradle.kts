plugins {
    kotlin("jvm") version "2.1.0"
}

group = "com.llamalad7"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven(url = "https://jitpack.io")
}

dependencies {
    implementation("io.ktor:ktor-client-core:3.0.1")
    implementation("io.ktor:ktor-client-java:3.0.1")
    implementation("ch.qos.logback:logback-classic:1.5.12")
    implementation("io.github.cdimascio:dotenv-kotlin:6.4.0")
    implementation("com.github.aballano:mnemonik:2.1.1")
    implementation(project.files("z3/com.microsoft.z3.jar"))
    implementation("guru.nidi:graphviz-kotlin:0.18.1")
    implementation("org.jgrapht:jgrapht-core:1.5.2")
    implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.8")
}