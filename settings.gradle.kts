enableFeaturePreview("GRADLE_METADATA")
include(":kdux-core", ":kdux-android",":example-app")

rootProject.name = "kdux-project"

pluginManagement {
  addRepositories(repositories)

  resolutionStrategy {
    eachPlugin {
      val module = when {
        requested.id.namespace == "com.android" -> "com.android.tools.build:gradle:${Versions.Plugins.Android}"
        else -> null
      }

      logger.quiet("Plugin requested (${requested.id.namespace}/${requested.id.name}): ${module}")
      if (module != null) {
        useModule(module)
      }

    }
  }
}
