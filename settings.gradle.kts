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

    gradlePluginPortal()
  }
}
dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    mavenLocal()
    google()
    mavenCentral()
    maven { setUrl("https://jitpack.io") }

  }
}


rootProject.name = "kdux-project"
include(":kdux-core", ":kdux-android",":example-app")
