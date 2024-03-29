plugins {
    kotlin("jvm") version "1.8.0"
    application
}

application {
    mainClass.set("com.intellij.indexing.shared.MainKt")
}

group = "com.jetbrains.intellij.indexing.shared"
version = "0.9.8"

repositories {
    mavenCentral()
    maven {
        url = uri("https://packages.jetbrains.team/maven/p/ij/intellij-shared-indexes")
    }
}

dependencies {
    implementation("com.jetbrains.intellij.indexing.shared:ij-shared-indexes-tool:0.9.9")
    implementation("ch.qos.logback:logback-classic:1.4.6")
}

kotlin {
    jvmToolchain(11)
}