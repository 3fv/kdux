import java.util.Properties
import java.io.StringReader
//import Script.Versions

object Versions {
  object Plugins {
    val Android = "3.5.4"
    val Kotlin = "1.3.21"
    val KotlinFrontend = "0.0.45"
    val Bintray = "1.8.4"
    val Node = "1.3.1"
  }
}
buildscript {
//  apply(from = "ko-gradle.gradle")
//
//  repositories {
//    flatDir {
//      dirs("${buildDir}/ko-repo/ko-artifact")
//    }
//  }
//
//  dependencies {
//    classpath("ko-artifact:ko:1.0.0")
//  }
}

plugins {
  base
  java
  `kotlin-dsl`
}

repositories {
  google()
  jcenter()
  mavenCentral()
  maven {
    url = uri("https://groovy.jfrog.io/artifactory/libs-release")
  }
  maven(url = "https://plugins.gradle.org/m2")
  maven {
    setUrl("https://dl.bintray.com/kotlin/kotlin-eap")
  }
  mavenLocal()
}

dependencies {
  "compileOnly"(gradleApi())
  "compileOnly"("com.android.tools.build:gradle:${Versions.Plugins.Android}")
  "runtime"("com.android.tools.build:gradle:${Versions.Plugins.Android}")
  "compile"("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.Plugins.Kotlin}")

  "compile"("com.jfrog.bintray.gradle:gradle-bintray-plugin:${Versions.Plugins.Bintray}")

}