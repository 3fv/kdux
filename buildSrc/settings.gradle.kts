import org.gradle.kotlin.dsl.maven
import java.io.File
import java.util.*

pluginManagement {

  repositories {
    jcenter()
    mavenCentral()
    gradlePluginPortal()
    maven(url = "https://plugins.gradle.org/m2")
    maven(url = uri("https://groovy.jfrog.io/artifactory/libs-release"))
//    maven(url = "https://dl.bintray.com/densebrain/oss")

  }

}

dependencyResolutionManagement {
  versionCatalogs {
    create("libs") {
      // point at the same TOML you use in the root build
      from(files("../gradle/libs.versions.toml"))
    }
  }
}
