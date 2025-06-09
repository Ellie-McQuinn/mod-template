plugins {
    id("template-shared")
    id("net.neoforged.moddev")
}

neoForge {
    neoFormVersion = Constants.NEOFORM_VERSION

    parchment {
        minecraftVersion = Constants.PARCHMENT_MINECRAFT
        mappingsVersion = Constants.PARCHMENT_RELEASE
    }
}

dependencies {
    compileOnly(group = "org.spongepowered", name = "mixin", version = Constants.MIXIN_VERSION)
    annotationProcessor(compileOnly(group = "io.github.llamalad7", name = "mixinextras-common", version = Constants.MIXIN_EXTRAS_VERSION))

    compileOnly(group = "thedarkcolour", name = "kotlinforforge-neoforge", version = Constants.NEOFORGE_KOTLIN_VERSION)
}

configurations {
    consumable("commonJava")
    consumable("commonKotlin")
    consumable("commonResources")
}

afterEvaluate {
    with(sourceSets.main.get()) {
        artifacts {
            java.sourceDirectories.forEach { add("commonJava", it) }
            kotlin.sourceDirectories.forEach { add("commonKotlin", it) }
            resources.sourceDirectories.forEach { add("commonResources", it) }
        }
    }
}