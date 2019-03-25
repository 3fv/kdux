enableFeaturePreview("GRADLE_METADATA")
//include(":example-app", ":kdux-core", ":example-lib", ":kdux-android")
include(":kdux-core", ":kdux-android")

rootProject.name = "kdux-project"
extra.set("version", File(rootProject.projectDir,"version.txt").readText())
extra.set("binTrayKey", System.getenv("BINTRAY_API_KEY") ?: "")

pluginManagement {
  repositories {
    google()
    gradlePluginPortal()
    jcenter()
    mavenCentral()
    maven {
      url = java.net.URI("https://dl.bintray.com/kotlin/kotlin-eap")
    }
  }
  resolutionStrategy {
    eachPlugin {
      println("Requested plugin: ${requested.id.name} / ${requested.id.namespace}")
      if(requested.id.namespace == "com.android") {
        useModule("com.android.tools.build:gradle:${Versions.android.gradle}")
      } else if (requested.id.namespace == "com.jfrog") {
        useModule("com.jfrog.bintray.gradle:gradle-bintray-plugin:${Versions.bintray}")
      }
    }
  }
}

