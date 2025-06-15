import quest.toybox.template.Constants

plugins {
    id("template-child")
    id("net.neoforged.moddev")
}

neoForge {
    version = Constants.NEOFORGE_VERSION

    accessTransformers.from(project(":common").neoForge.accessTransformers.files)

    parchment {
        minecraftVersion = Constants.PARCHMENT_MINECRAFT
        mappingsVersion = Constants.PARCHMENT_RELEASE
    }

    runs {
        configureEach {
            systemProperty("neoforge.enabledGameTestNamespaces", Constants.MOD_ID)
        }

        create("client") {
            client()
            ideName = "NeoForge Client"
        }

        create("data") {
            data()
            ideName = "NeoForge Data"

            programArguments.addAll(
                "--mod", Constants.MOD_ID,
                "--output", file("src/generated/resources").absolutePath,
                "--existing", findProject(":common")!!.file("src/main/resources").absolutePath,
                "--all"
            )
        }

        create("server") {
            server()
            ideName = "NeoForge Server"
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

publishMods {
    file = tasks.jar.map { it.archiveFile.get() }
}