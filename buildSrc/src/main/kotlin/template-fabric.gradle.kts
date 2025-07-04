import quest.toybox.template.Constants

plugins {
    id("template-platform")
    id("fabric-loom")
    id("quest.toybox.sculptor-fabric")
}

dependencies {
    modImplementation("net.fabricmc:fabric-loader:${Constants.FABRIC_LOADER_VERSION}")
}

fabricApi {
    configureDataGeneration {
        modId = Constants.MOD_ID
        outputDirectory = file("src/generated/resources")
    }
}

loom {
    val accessWidener = project(":common").file("src/main/resources/${Constants.MOD_ID}.accesswidener")
    if (accessWidener.exists()) {
        accessWidenerPath = accessWidener
    }

    @Suppress("UnstableApiUsage")
    mixin {
        defaultRefmapName = "${Constants.MOD_ID}.refmap.json"
        useLegacyMixinAp = false
    }

    runs {
        named("client") {
            client()

            configName = "Fabric Client"
            isIdeConfigGenerated = true
        }

        named("server") {
            server()

            configName = "Fabric Server"
            isIdeConfigGenerated = true
        }

        named("datagen") {
            configName = "Fabric Data"
            isIdeConfigGenerated = true
        }
    }
}

tasks {
    processResources {
        exclude("META-INF/accesstransformer.cfg")
    }

    remapJar {
        archiveVersion = Constants.getModVersion()
    }
}