plugins {
    idea
    `java-library`
}

// Find checksums here: https://gradle.org/release-checksums/
// Run gradlew :wrapper a couple of times to update.
tasks.wrapper {
    gradleVersion = "8.14.2"
    distributionSha256Sum = "7197a12f450794931532469d4ff21a59ea2c1cd59a3ec3f89c035c3c420a6999"
    distributionType = Wrapper.DistributionType.BIN
}

gradle.taskGraph.whenReady {
    if (!providers.environmentVariable("MULTILOADER_DRY_RUN").isPresent){
        if (hasTask("publishMods") && !providers.environmentVariable("CI").isPresent) {
            throw IllegalStateException("Cannot publish mods locally, please run the release workflow on GitHub.")
        }
    }
}

idea {
    module {
        isDownloadSources = true
        isDownloadJavadoc = true
    }
}
