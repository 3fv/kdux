plugins {
  base
  java
  `kotlin-dsl`
}

repositories {
  google()
  mavenCentral()
  maven(url = "https://plugins.gradle.org/m2")

  mavenLocal()
}

dependencies {
  compileOnly(gradleApi())
  compileOnly(libs.android.build.tools)
  compileOnly(libs.kotlin.gradle.plugin)

  implementation(libs.semver)


}