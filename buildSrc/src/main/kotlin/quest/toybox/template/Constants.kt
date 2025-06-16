package quest.toybox.template

import org.codehaus.groovy.runtime.ProcessGroovyMethods
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.kotlin.dsl.support.uppercaseFirstChar

object Constants {
    const val GROUP = "quest.toybox.template"
    const val MOD_ID = "template"
    const val MOD_NAME = "Template Mod"
    const val MOD_VERSION = "2101.1.0.0"
    const val LICENSE = "MIT-0"
    const val DESCRIPTION = "A modern extensible modding template for all of our mods."

    const val HOMEPAGE = "https://modrinth.com/mod/mod-template"
    const val ISSUE_TRACKER = "https://github.com/Ellie-McQuinn/mod-template/issues"
    const val SOURCES_URL = "https://github.com/Ellie-McQuinn/mod-template"

    val curseforgeProperties: CurseForgeProperties? = object : CurseForgeProperties() {
        override val projectId = "000000"
        override val projectSlug = "mod-template"
    }

    val modrinthProperties: ModrinthProperties? = object : ModrinthProperties() {
        override val projectId: String = "XXXXXXXX"
    }

    val githubProperties: GithubProperties? = object : GithubProperties() {
        override val repo: String = "Ellie-McQuinn/mod-template"
    }

    const val PUBLISH_WEBHOOK_VARIABLE = "PUBLISH_WEBHOOK"

    const val COMPARE_URL = "https://github.com/Ellie-McQuinn/mod-template/compare/"

    val CONTRIBUTORS = linkedMapOf(
        "Ellie McQuinn / Toybox System" to "Project Owner"
    )

    val CREDITS = linkedMapOf<String, String>(

    )

    val EXTRA_MOD_INFO_REPLACEMENTS = mapOf<String, String>(

    )

    val JAVA_VERSION = JavaLanguageVersion.of(21)
    const val JETBRAIN_ANNOTATIONS_VERSION = "26.0.2"

    const val MIXIN_VERSION = "0.13.3+mixin.0.8.5"
    const val MIXIN_EXTRAS_VERSION = "0.3.5"

    const val MINECRAFT_VERSION = "1.21.5"
    const val FL_MINECRAFT_CONSTRAINT = ">=1.21.5"
    const val NF_MINECRAFT_CONSTRAINT = "[1.21.5,)"

    // https://parchmentmc.org/docs/getting-started#choose-a-version/
    const val PARCHMENT_MINECRAFT = "1.21.5"
    const val PARCHMENT_RELEASE = "2025.06.15"

    // https://fabricmc.net/develop/
    const val FABRIC_API_VERSION = "0.127.0+1.21.5"
    const val FABRIC_KOTLIN_VERSION = "1.13.3+kotlin.2.1.21"
    const val FABRIC_LOADER_VERSION = "0.16.14"

    const val NEOFORM_VERSION = "1.21.5-20250325.162830" // // https://projects.neoforged.net/neoforged/neoform/
    const val NEOFORGE_VERSION = "21.5.75" // https://projects.neoforged.net/neoforged/neoforge/
    const val NEOFORGE_KOTLIN_VERSION = "5.9.0"
    const val FML_CONSTRAINT = "[4,)" // https://projects.neoforged.net/neoforged/fancymodloader/

    fun getProjectName(project: Project): String {
        return project.name.uppercaseFirstChar()
    }

    fun getModVersion(): String {
        return if (MOD_VERSION.count { it == '.' } == 3) {
            MOD_VERSION.substringAfter('.')
        } else {
            MOD_VERSION
        }
    }

    fun getChangelog(project: Project): Provider<String> {
        return project.providers.provider {
            val compareTag = ProcessGroovyMethods.getText(ProcessGroovyMethods.execute("git describe --tags --abbrev=0")).trim()
            val commitHash = ProcessGroovyMethods.getText(ProcessGroovyMethods.execute("git rev-parse HEAD")).trim()

            buildString {
                appendLine(project.rootDir.resolve("changelog.md").readText(Charsets.UTF_8).trimEnd())

                if (compareTag.isNotBlank()) {
                    appendLine()
                    val url = "${COMPARE_URL}${compareTag.replace("+", "%2B")}...${commitHash}"
                    appendLine("A detailed changelog can be found [here](<$url>).")
                }
            }
        }
    }
}
