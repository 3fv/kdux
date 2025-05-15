import java.net.URI

pluginManagement {
  repositories {
    mavenLocal()

    google {
      content {
        includeGroupByRegex("com\\.android.*")
        includeGroupByRegex("com\\.google.*")
        includeGroupByRegex("androidx.*")
      }
    }

    mavenCentral()
    maven { setUrl("https://jitpack.io") }
    maven { url = uri("https://plugins.gradle.org/m2") }
    maven { url = uri("https://groovy.jfrog.io/artifactory/libs-release") }
    maven { url = uri("https://maven.pkg.github.com/3fv/kdux") }

    gradlePluginPortal()
  }
}
dependencyResolutionManagement {
  @Suppress("UnstableApiUsage")
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  @Suppress("UnstableApiUsage")
  repositories {
    mavenLocal()
    google()
    mavenCentral()
    maven { setUrl("https://jitpack.io") }
    maven { url = uri("https://maven.pkg.github.com/3fv/kdux") }
  }
}


rootProject.name = "kdux-project"
include(":kdux-core", ":kdux-android",":example-app")
