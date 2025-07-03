import quest.toybox.sculptor.extension.FeatureKey
import quest.toybox.template.Constants

plugins {
    id("template-parent")
    id("quest.toybox.sculptor-common")
}

dependencies {
    compileOnly(group = "net.fabricmc", name = "sponge-mixin", version = Constants.MIXIN_VERSION)
    annotationProcessor(compileOnly(group = "io.github.llamalad7", name = "mixinextras-common", version = Constants.MIXIN_EXTRAS_VERSION))

    compileOnly(group = "thedarkcolour", name = "kotlinforforge-neoforge", version = Constants.NEOFORGE_KOTLIN_VERSION)
}

sculptor {
    flag(FeatureKey.DATA_GENERATION, Unit)
}