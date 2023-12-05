pluginManagement {
    includeBuild("gradle/meta-plugins")
}
rootProject.name = "carl-blogs"

include("it", "cb-relations:cb-relations-kv:cb-relations-graph")
include("cb-protobuf")


// base 系列
//include("cb-base:cb-base-scylla")

for (projectName in listOf("scylla")) {
    val fullProjectName = "cb-base-${projectName}"
    include(fullProjectName)
    project(":$fullProjectName").projectDir = File("./cb-base/${projectName}")

}