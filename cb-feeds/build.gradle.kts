plugins {
    id("cb-kotlin")
}

//noArg {
//    annotation("com.cb.releations.kv.infra.KotlinNoArgs")
//    invokeInitializers = true
//}

dependencies {
    implementation(project(":cb-protobuf"))
    implementation(project(":cb-base-scylla"))
    implementation(project(":cb-base-core"))
    implementation("com.linecorp.armeria:armeria-protobuf:")
    implementation("com.linecorp.armeria:armeria-grpc:")
    implementation("org.springframework.boot:spring-boot-starter-web:")
    implementation("org.springframework.boot:spring-boot-starter-aop:")


    kapt("com.scylladb:java-driver-mapper-processor:4.15.0.1")
}