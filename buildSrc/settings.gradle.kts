import org.gradle.kotlin.dsl.maven
import java.io.File
import java.util.*

pluginManagement {

  repositories {
    jcenter()
    mavenCentral()
    gradlePluginPortal()
    maven(url = "https://plugins.gradle.org/m2")
    maven(url = "https://dl.bintray.com/densebrain/oss")

  }

  resolutionStrategy {
    eachPlugin {
      when {
        requested.id.namespace?.startsWith("org.jetbrains") == true ->
          useModule("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.21")
      }
    }
  }
}