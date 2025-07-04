package quest.toybox.template

import org.gradle.api.JavaVersion

abstract class CurseForgeProperties {
    abstract val projectId: String
    abstract val projectSlug: String
    open val uploadToken: String = "CURSEFORGE_TOKEN"
    open val serverSideRequired = true
    open val clientSideRequired = true
    open val supportedJavaVersions = listOf(JavaVersion.VERSION_21, JavaVersion.VERSION_22)
    open val supportedMinecraftVersions = listOf(Constants.MINECRAFT_VERSION.getCurseForgeName())
}
