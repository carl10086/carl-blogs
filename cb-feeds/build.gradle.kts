import org.jetbrains.kotlin.fir.expressions.builder.buildArgumentList

plugins {
    id("cb-kotlin")
//    id("org.graalvm.buildtools.native") version ("0.9.27")
//    id("org.springframework.boot") version ("3.2.0")
}

//noArg {
//    annotation("com.cb.releations.kv.infra.KotlinNoArgs")
//    invokeInitializers = true
//}


//graalvmNative {
//    binaries {
//        getByName("main") {
//            buildArgs.add("--enable-preview")
//            buildArgs.add("--initialize-at-run-time=com.datastax.oss.driver.internal.core")
//            buildArgs.add("--initialize-at-run-time=com.datastax.oss.driver.internal.core.util.Dependency")
//            buildArgs.add("--initialize-at-run-time=com.datastax.dse.driver.internal.core.type.codec")
//            buildArgs.add("--trace-class-initialization=com.datastax.oss.driver.internal.core.util.Dependency")
//
//        }
//    }
//}
//

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