import me.modmuss50.mpp.PublishModTask
import me.modmuss50.mpp.PublishResult
import quest.toybox.template.Constants
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpRequest.BodyPublishers
import java.net.http.HttpResponse.BodyHandlers

plugins {
    idea
    `java-library`

    id("me.modmuss50.mod-publish-plugin")
}

// Find checksums here: https://gradle.org/release-checksums/
// Run gradlew :wrapper a couple of times to update.
tasks.wrapper {
    gradleVersion = "8.14.2"
    distributionSha256Sum = "7197a12f450794931532469d4ff21a59ea2c1cd59a3ec3f89c035c3c420a6999"
    distributionType = Wrapper.DistributionType.BIN
}

gradle.taskGraph.whenReady {
    if (!providers.environmentVariable("MULTILOADER_DRY_RUN").isPresent){
        if (hasTask("publishMods") && !providers.environmentVariable("CI").isPresent) {
            throw IllegalStateException("Cannot publish mods locally, please run the release workflow on GitHub.")
        }
    }
}

publishMods {
    Constants.githubProperties?.also { props ->
        github {
            accessToken = providers.environmentVariable(props.uploadToken)
            repository = props.repo
            commitish = "main-${Constants.MINECRAFT_VERSION}"
            tagName = "${Constants.getModVersion()}+${Constants.MINECRAFT_VERSION}"
        }
    }
}

tasks.publishMods {
    doLast {
        val environmentVariable = providers.environmentVariable(Constants.PUBLISH_WEBHOOK_VARIABLE)

        if (!environmentVariable.isPresent) {
            return@doLast
        }

        val webhookUrl = uri(environmentVariable.get())

        val results = setOf("publishCurseforge", "publishModrinth").associateWith {
            listOf(project(":neoforge"), project(":fabric"))
        }.mapValues { (key, projects) ->
            projects.associateWith { it.tasks.getByName<PublishModTask>(key) }
                .mapKeys { (project, task) -> Constants.getProjectName(project) }
                .map { (project, task) -> "[${project}](<${PublishResult.fromJson(task.result.get().asFile.readText()).link}>)" }
        }

        val payload = buildString {
            append("""{"content": """")

            if (publishMods.dryRun.get()) {
                append("""
                    |:warning: :warning: :warning:
                    |**DRY RUN**
                    |:warning: :warning: :warning:
                """.trimMargin().trim().replace("\n", "\\n"))
                append("\\n\\n")
            }

            append(
                """
                |**${Constants.MOD_NAME} ${Constants.getModVersion()}** for **${Constants.MINECRAFT_VERSION}**
                |${Constants.getChangelog(project).get()}
                |:curseforge: ${results["publishCurseForge"]!!.joinToString(" | ")}
                |:modrinth: ${results["publishModrinth"]!!.joinToString(" | ")}
            """.trimMargin().trim().replace("\n", "\\n"))

            append(""""}""")
        }

        val request = HttpRequest.newBuilder(webhookUrl).header("Content-Type", "application/json").POST(BodyPublishers.ofString(payload)).build()
        val response = HttpClient.newHttpClient().send(request, BodyHandlers.ofString())

        if (response.statusCode() !in 200..299) {
            project.logger.error("Failed to publish release notes to webhook:\n${response.body()}")
        }
    }
}

tasks.register("logVersion") {
    doFirst {
        println("mod-version=${Constants.getModVersion()}+${Constants.MINECRAFT_VERSION}")
    }
}

idea {
    module {
        isDownloadSources = true
        isDownloadJavadoc = true
    }
}
