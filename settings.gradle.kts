pluginManagement {
    includeBuild("gradle/meta-plugins")
}
rootProject.name = "carl-blogs"

include("it", "cb-relations:cb-relations-kv:cb-relations-graph")
include("cb-protobuf")
include("cb-feeds")


// base 系列
//include("cb-base:cb-base-scylla")

for (projectName in listOf("scylla", "core")) {
    val fullProjectName = "cb-base-${projectName}"
    include(fullProjectName)
    project(":$fullProjectName").projectDir = File("./cb-base/${projectName}")
}


for (projectName in listOf("netty")) {
    val fullProjectName = "cb-examples-${projectName}"
    include(fullProjectName)
    project(":$fullProjectName").projectDir = File("./cb-examples/${projectName}")
}