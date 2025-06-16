package quest.toybox.template

abstract class GithubProperties {
    abstract val repo: String

    open val uploadToken: String = "GITHUB_TOKEN"
}