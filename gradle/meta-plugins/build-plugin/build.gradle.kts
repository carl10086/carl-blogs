plugins {
    id("java")
    id("maven-publish")
    id("java-gradle-plugin")
    kotlin("kapt") version ("2.0.0") apply (false)
    id("org.springframework.boot") version ("3.3.2") apply (false)
    id("com.google.protobuf") version ("0.9.4") apply (false)
    kotlin("jvm") version "2.0.0"
}

gradlePlugin {
    plugins.create("cb-java-base") {
        id = name
        implementationClass = "com.cb.gradle.plugins.JavaBasePlugin"
    }

    plugins.create("cb-java-lib") {
        id = name
        implementationClass = "com.cb.gradle.plugins.JavaBaseLibraryPlugin"
    }

    plugins.create("cb-kotlin") {
        id = name
        implementationClass = "com.cb.gradle.plugins.KotlinBasePlugin"
    }

    plugins.create("cb-kotlin-app") {
        id = name
        implementationClass = "com.cb.gradle.plugins.KotlinAppPlugin"
    }



    plugins.create("cb-protobuf") {
        id = name
        implementationClass = "com.cb.gradle.plugins.JavaGrpcProtoBufPlugin"
    }

}


dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.21")
    implementation("org.springframework.boot:spring-boot-gradle-plugin:3.3.2")
    implementation("com.google.protobuf:protobuf-gradle-plugin:0.9.4")
    implementation(kotlin("stdlib-jdk8"))
}
repositories {
    mavenCentral()
}
kotlin {
    jvmToolchain(21)
}