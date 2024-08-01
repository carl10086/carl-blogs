pluginManagement {
    includeBuild("gradle/meta-plugins")
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "carl-blogs"

include("it", "cb-relations:cb-relations-kv")
include("cb-protobuf")
include("cb-feeds")


// base 系列
//include("cb-base:cb-base-scylla")

fun includeProject(basePath: String, projectNames: List<String>) {
    for (projectName in projectNames) {
        val fullProjectName = "${basePath}-${projectName}"
        include(fullProjectName)
        project(":$fullProjectName").projectDir = File("./${basePath}/${projectName}")
    }
}

includeProject("cb-base", listOf("scylla", "core", "redis"))
includeProject("cb-examples", listOf("netty", "opentelemetry", "flink", "web"))
includeProject("cb-sch", listOf("suggest", "recommend"))
includeProject("cb-act", listOf("consumer"))
