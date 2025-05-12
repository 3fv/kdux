import java.util.Properties
import java.io.StringReader

plugins {
  base
  java
  `kotlin-dsl`
}

repositories {
  google()
  mavenCentral()
  maven {
    url = uri("https://groovy.jfrog.io/artifactory/libs-release")
  }
  maven(url = "https://plugins.gradle.org/m2")

  mavenLocal()
}

dependencies {
  compileOnly(gradleApi())
  compileOnly(libs.android.build.tools)
  compileOnly(libs.kotlin.gradle.plugin)

  implementation(libs.semver)
  implementation(libs.jfrog.bintray.plugin)

}