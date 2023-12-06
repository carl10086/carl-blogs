plugins {
    id("cb-kotlin")
}

dependencies {
    api("jakarta.json:jakarta.json-api:")
    api("com.fasterxml.jackson.core:jackson-databind:")
    api("com.fasterxml.jackson.core:jackson-core:")
    api("com.fasterxml.jackson.module:jackson-module-kotlin:")
    api("org.slf4j:slf4j-api:")
//    0.9.13
    implementation("com.hubspot.jackson:jackson-datatype-protobuf:0.9.13")

    testImplementation(project(":cb-protobuf"))
}