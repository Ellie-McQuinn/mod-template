import quest.toybox.template.Constants

plugins {
    id("template-fabric")
}

template {
    mods {
        create("fabric-api") {
            required()

            artifacts {
                modImplementation(group = "net.fabricmc.fabric-api", name = "fabric-api", version = Constants.FABRIC_API_VERSION)
            }
        }

        create("fabric-language-kotlin") {
            required()

            artifacts {
                modImplementation(group = "net.fabricmc", name = "fabric-language-kotlin", version = Constants.FABRIC_KOTLIN_VERSION)
            }
        }
    }
}
