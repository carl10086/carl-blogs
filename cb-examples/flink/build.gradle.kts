plugins {
    id("cb-java-lib")
    id("com.github.johnrengelman.shadow") version ("7.1.2")
}

apply(plugin = "com.github.johnrengelman.shadow")

val flinkVersion = "1.18.0"



dependencies {
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:")
    implementation("org.apache.logging.log4j:log4j-api:")
    implementation("org.apache.logging.log4j:log4j-core:")
    implementation("org.apache.flink:flink-streaming-java:$flinkVersion")
    implementation("org.apache.flink:flink-clients:$flinkVersion")
    implementation("org.apache.flink:flink-java:$flinkVersion")
}
