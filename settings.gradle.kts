pluginManagement {
    includeBuild("gradle/meta-plugins")
}
rootProject.name = "carl-blogs"

include("it", "cb-relations:cb-relations-kv:cb-relations-graph")
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
includeProject("cb-examples", listOf("netty", "opentelemetry"))
includeProject("cb-sch", listOf("es"))
