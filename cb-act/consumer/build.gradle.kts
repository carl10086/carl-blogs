plugins {
    id("cb-kotlin")
}


tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.add("--enable-preview")
}


dependencies {
    implementation(project(":cb-protobuf"))
    implementation(project(":cb-base-scylla"))
    implementation(project(":cb-base-core"))
    implementation("com.linecorp.armeria:armeria-protobuf:")
    implementation("com.linecorp.armeria:armeria-grpc:")
    implementation("org.lz4:lz4-java:1.7.1")
    implementation("org.springframework.boot:spring-boot-starter-web:")
    implementation("org.springframework.boot:spring-boot-starter-aop:")

    testImplementation("org.springframework.boot:spring-boot-starter-test")

    kapt("com.scylladb:java-driver-mapper-processor:4.15.0.1")
}