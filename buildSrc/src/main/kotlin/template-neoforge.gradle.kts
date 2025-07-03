import quest.toybox.sculptor.extension.FeatureKey
import quest.toybox.template.Constants

plugins {
    id("template-platform")
    id("quest.toybox.sculptor-neoforge")
}

neoForge {
    version = Constants.NEOFORGE_VERSION

    accessTransformers.from(project(":common").neoForge.accessTransformers.files)

    runs {
        configureEach {
            systemProperty("neoforge.enabledGameTestNamespaces", Constants.MOD_ID)
        }

        create("client") {
            client()
            ideName = "NeoForge Client (:neoforge)"
        }

        val common = findProject(":common")!!

        create("commonData") {
            if (sculptor.get(FeatureKey.MINECRAFT).map { it.hasSplitDatagens() }.get()) {
                clientData()
            } else {
                data()
            }

            ideName = "Common Data (:neoforge)"

            programArguments.addAll(
                "--mod", Constants.MOD_ID,
                "--output", common.file("src/generated/resources").absolutePath,
                "--existing", common.file("src/main/resources").absolutePath,
                "--all"
            )

            systemProperty("template.datagen.common", "true")
        }

        create("data") {
            if (sculptor.get(FeatureKey.MINECRAFT).map { it.hasSplitDatagens() }.get()) {
                clientData()
            } else {
                data()
            }

            ideName = "NeoForge Data (:neoforge)"

            programArguments.addAll(
                "--mod", Constants.MOD_ID,
                "--output", file("src/generated/resources").absolutePath,
                "--existing", common.file("src/main/resources").absolutePath,
                "--all"
            )
        }

        create("server") {
            server()
            ideName = "NeoForge Server (:neoforge)"
        }
    }

    mods {
        create(Constants.MOD_ID) {
            sourceSet(sourceSets.main.get())
        }
    }
}

sourceSets.main {
    resources.srcDirs("src/generated/resources")
}

tasks.processResources {
    exclude("*.accesswidener")
}
