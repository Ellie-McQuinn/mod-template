import com.google.gson.JsonObject
import me.modmuss50.mpp.ReleaseType
import quest.toybox.template.Constants
import quest.toybox.template.extension.DependencyType
import quest.toybox.template.extension.TemplateExtension
import quest.toybox.template.extension.UploadTarget
import quest.toybox.template.task.JsonProcessingReader

plugins {
    id("template-shared")
    id("me.modmuss50.mod-publish-plugin")
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

fun getReleaseType(version: String): ReleaseType =
    if ("alpha" in version) { ReleaseType.ALPHA }
    else if ("beta" in version) { ReleaseType.BETA }
    else { ReleaseType.STABLE }

fun boolean(provider: Provider<String>): Provider<Boolean> {
    return provider.map { it.equals("true", true) }
}

val template = extensions.getByType<TemplateExtension>()

publishMods {
    changelog = Constants.getChangelog(project)

    type = getReleaseType(Constants.MOD_VERSION)

    dryRun = boolean(providers.environmentVariable("MULTILOADER_DRY_RUN"))

    displayName = "${Constants.getProjectName(project)} ${Constants.getModVersion()}+${Constants.MINECRAFT_VERSION}"

    modLoaders.add(project.name)

    version = Constants.MOD_VERSION

    Constants.curseforgeProperties?.also { props ->
        curseforge {
            accessToken = providers.environmentVariable(props.uploadToken)
            projectId = props.projectId
            projectSlug = props.projectSlug
            clientRequired = props.clientSideRequired
            serverRequired = props.serverSideRequired
            javaVersions = props.supportedJavaVersions
            minecraftVersions = props.supportedMinecraftVersions

            dependencies {
                requires(*template.getDependencyIds(UploadTarget.CURSEFORGE, DependencyType.REQUIRED).toTypedArray())
                optional(*template.getDependencyIds(UploadTarget.CURSEFORGE, DependencyType.OPTIONAL).toTypedArray())
            }
        }
    }

    Constants.modrinthProperties?.also { props ->
        modrinth {
            accessToken = providers.environmentVariable(props.uploadToken)
            projectId = props.projectId
            minecraftVersions = props.supportedMinecraftVersions

            dependencies {
                requires(*template.getDependencyIds(UploadTarget.MODRINTH, DependencyType.REQUIRED).toTypedArray())
                optional(*template.getDependencyIds(UploadTarget.MODRINTH, DependencyType.OPTIONAL).toTypedArray())
            }
        }
    }

    Constants.githubProperties?.also { props ->
        github {
            accessToken = providers.environmentVariable(props.uploadToken)

            parent(rootProject.tasks.named("publishGithub"))
        }
    }
}