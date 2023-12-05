plugins {
    id("cb-kotlin")
}

dependencies {
    implementation(project(":cb-base-scylla"))
    implementation(project(":cb-relations:cb-relations-kv"))

    testImplementation("org.springframework:spring-context:")
}