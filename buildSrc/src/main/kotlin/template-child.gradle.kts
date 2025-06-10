import com.google.gson.JsonObject
import quest.toybox.template.Constants
import quest.toybox.template.task.JsonProcessingReader

plugins {
    id("template-shared")
}

evaluationDependsOn(":common")

configurations {
    create("commonJava") { isCanBeResolved = true }
    create("commonKotlin") { isCanBeResolved = true }
    create("commonResources") { isCanBeResolved = true }
}

dependencies {
    compileOnly(project(":common"))

    "commonJava"(project(path = ":common", configuration = "commonJava"))
    "commonKotlin"(project(path = ":common", configuration = "commonKotlin"))
    "commonResources"(project(path = ":common", configuration = "commonResources"))
}

tasks {
    compileJava {
        dependsOn(configurations["commonJava"])
        source(configurations["commonJava"])
    }

    compileKotlin {
        dependsOn(configurations["commonKotlin"])
        source(configurations["commonKotlin"])
    }

    processResources {
        dependsOn(configurations["commonResources"])
        from(configurations["commonResources"]) {
            exclude("fabric.mod.json")
        }

        filesMatching(listOf("**/*.json", "**/*.mcmeta")) {
            val processor: (JsonObject.() -> Unit)? = when (name) {
                "fabric.mod.json" -> { ->
                    val authors = getAsJsonArray("authors")
                    val contributors = getAsJsonArray("contributors")

                    for ((contributor, role) in Constants.CONTRIBUTORS) {
                        if (role == "Project Owner") {
                            authors.add(contributor)
                        } else {
                            contributors.add(contributor)
                        }
                    }
                }
                else -> null
            }

            filter(mapOf("processor" to processor), JsonProcessingReader::class.java)
        }
    }
}