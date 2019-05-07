enableFeaturePreview("GRADLE_METADATA")
//include(":example-app", ":kdux-core", ":example-lib", ":kdux-android")
include(":kdux-core", ":kdux-android", ":kdux-android-flipper-plugin", ":kdux-flipper-plugin", ":example-app")


rootProject.name = "kdux-project"

pluginManagement {
  addRepositories(repositories)

  resolutionStrategy {
    eachPlugin {

      val module = when {
        requested.id.namespace == "com.android" -> "com.android.tools.build:gradle:${Versions.Plugins.Android}"
        requested.id.namespace == "com.jfrog" -> "com.jfrog.bintray.gradle:gradle-bintray-plugin:${Versions.Plugins.Bintray}"
        requested.id.namespace == "org.jetbrains.kotlin.frontend" -> "org.jetbrains.kotlin:kotlin-frontend-plugin:${Versions.Plugins.KotlinFrontend}"
        requested.id.namespace?.startsWith("org.jetbrains.kotlin") == true -> "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.Plugins.Kotlin}"
        else -> null
      }

      logger.quiet("Plugin requested (${requested.id.namespace}/${requested.id.name}): ${module}")
      if (module != null) {
        useModule(module)
      }

    }
  }
}

//val flipperDir = property("flipper.dir")
//logger.quiet("Flipper dir: ${flipperDir}")
//flipperDir?.let {
//  val dir = File(it)
//  if (dir.exists() && dir.isDirectory) {
//    includeBuild(dir) {
//      dependencySubstitution {
//        substitute(module(Deps.android.fb.flipperPrefix)).with(project(":android"))
//        substitute(module(Deps.android.fb.fbjniPrefix)).with(project(":fbjni"))
//        substitute(project(":android")).with(project(":android"))
//      }
//    }
//  }
//}
//
