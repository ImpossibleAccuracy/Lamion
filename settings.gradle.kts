rootProject.name = "Lamion"

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        google()
    }
}

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        mavenCentral()
        gradlePluginPortal()
        google()
    }
}

include(
    ":server:shared",
    ":server:worker",
    ":server:app",
)
