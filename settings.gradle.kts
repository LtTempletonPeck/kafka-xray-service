pluginManagement {
    val nexusUrl: String by settings
    repositories {
        maven("$nexusUrl/maven-public")
    }
}

rootProject.name = "kafka-xray-service"
