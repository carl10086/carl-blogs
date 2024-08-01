import org.jetbrains.kotlin.cli.jvm.compiler.findMainClass

plugins {
    id("cb-kotlin-app")
}

tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    mainClass.set("com.cb.example.web.ExampleAppKt")
//    archiveFileName.set("")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web:")
    implementation("org.springframework.boot:spring-boot-starter-aop:")
}