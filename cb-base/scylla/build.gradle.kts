plugins {
    id("cb-kotlin")
}

dependencies {
//    val scyllaVersion = "4.17.0.0"
    val scyllaVersion = "4.15.0.1"
    implementation("io.netty:netty-all:")

    api("com.scylladb:java-driver-core:${scyllaVersion}")
    api("com.scylladb:java-driver-query-builder:${scyllaVersion}")
    api("com.scylladb:java-driver-mapper-runtime:${scyllaVersion}")

    compileOnly("org.springframework:spring-context:")
}