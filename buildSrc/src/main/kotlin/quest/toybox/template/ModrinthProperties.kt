package quest.toybox.template

abstract class ModrinthProperties {
    abstract val projectId: String
    open val uploadToken: String = "MODRINTH_TOKEN"
}