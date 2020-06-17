import org.gradle.plugins.ide.idea.model.IdeaLanguageLevel
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val kotlinVersion = "1.3.72"
    val nebulaProjectVersion = "7.0.9"
    val springBootVersion = "2.2.8.RELEASE"
    val springDependencyManagementVersion = "1.0.9.RELEASE"

    idea
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    id("nebula.kotlin") version kotlinVersion
    id("io.spring.dependency-management") version springDependencyManagementVersion
    id("org.springframework.boot") version springBootVersion
    id("nebula.project") version nebulaProjectVersion
}

val nexusUrl: String by project

val kotlinLoggingVersion = "1.7.9"
val mockitoKotlinVersion = "2.2.0"
val springCloudVersion = "Hoxton.SR5"
val zipkinAwsVersion = "0.21.2"

version = System.getenv("PROJECT_VERSION") ?: "0.0.0-SNAPSHOT"

java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenLocal()
    maven("$nexusUrl/maven-public")
}

configurations {
    all { resolutionStrategy.cacheChangingModulesFor(0, TimeUnit.SECONDS) }
    "testImplementation" { exclude(group = "org.junit.vintage", module = "junit-vintage-engine") }
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:$springCloudVersion")
    }
}

dependencies {
    implementation(kotlin("reflect"))

    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")

    implementation("org.springframework.cloud:spring-cloud-starter-stream-kafka")
    implementation("org.springframework.cloud:spring-cloud-starter-zipkin")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.github.microutils:kotlin-logging:$kotlinLoggingVersion")
    implementation("io.zipkin.aws:zipkin-reporter-xray-udp:$zipkinAwsVersion")
    implementation("io.zipkin.aws:brave-propagation-aws:$zipkinAwsVersion")

    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:$mockitoKotlinVersion")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.cloud:spring-cloud-stream-test-support")
}

idea {
    module.isDownloadJavadoc = true
    project {
        vcs = "Git"
        languageLevel = IdeaLanguageLevel(java.sourceCompatibility)
    }
}

tasks {
    withType<KotlinCompile>().configureEach {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            javaParameters = true
            jvmTarget = java.sourceCompatibility.name
        }
    }
    test {
        failFast = true
        useJUnitPlatform()
    }
}
