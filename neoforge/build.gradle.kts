import quest.toybox.template.Constants

plugins {
    id("template-neoforge")
}

configurations {
    val localRuntime = create("localRuntime")
    runtimeClasspath.configure { extendsFrom(localRuntime) }
}

fun DependencyHandler.localRuntime(notation: Any) {
    add("localRuntime", notation)
}

template {
    mods {
        create("kotlin-for-forge") {
            required()

            artifacts {
                implementation("thedarkcolour:kotlinforforge-neoforge:${Constants.NEOFORGE_KOTLIN_VERSION}")
            }
        }
    }
}
