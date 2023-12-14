plugins {
    id("cb-kotlin")
}

dependencies {
    implementation(project(":cb-base-core"))
    implementation(("io.lettuce:lettuce-core:"))
}