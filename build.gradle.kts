import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import java.net.URI
import java.nio.charset.StandardCharsets
import java.util.*

plugins {
    kotlin("jvm") version "2.0.0"

    id("java")

    id("maven-publish")
    id("com.gradleup.shadow") version "8.3.0"
}

group = "net.cakemc.library"
version = "0.0.0-develop"

var repoProperties = Properties()
var repoFile = file("/credentials.properties")
if (repoFile.exists())
    repoProperties.load(repoFile.inputStream())
var repoUsername = repoProperties.getProperty("username", System.getenv("REPOSITORY_USERNAME"))
var repoPassword = repoProperties.getProperty("password", System.getenv("REPOSITORY_PASSWORD"))

repositories {
    mavenLocal()
    mavenCentral()
    maven {
        name = "cakemc"
        url = URI.create("http://cakemc.net:8081/releases")
        credentials {
            username = repoUsername
            password = repoPassword
        }
        isAllowInsecureProtocol = true
    }
}

val jdkVersion = JavaVersion.VERSION_21
val jdkVersionString = jdkVersion.toString()

java {
    toolchain.languageVersion = JavaLanguageVersion.of(jdkVersionString)
    withSourcesJar()
}

tasks.withType<JavaCompile> {
    sourceCompatibility = jdkVersionString
    targetCompatibility = jdkVersionString
    options.encoding = StandardCharsets.UTF_8.toString()
}

tasks.withType<AbstractArchiveTask> {
    isReproducibleFileOrder = true
    isPreserveFileTimestamps = false
}

tasks.withType<ShadowJar> {
    configurations = listOf(project.configurations.shadow.get())
    isZip64 = true
}

configurations.shadow { isTransitive = false }

publishing {
    publications.create<MavenPublication>(rootProject.name) {
        artifact(tasks.shadowJar)
    }
    repositories {
        maven {
            name = "cakemc"
            url = URI.create("http://cakemc.net:8081/releases")
            credentials {
                username = repoUsername
                password = repoPassword
            }
            isAllowInsecureProtocol = true
        }
    }
}

kotlin {
    jvmToolchain(21)
}