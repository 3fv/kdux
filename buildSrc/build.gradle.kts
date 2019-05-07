import java.util.Properties
import java.io.StringReader
import Script.Versions

buildscript {
  apply(from = "ko-gradle.gradle")

  repositories {
    flatDir {
      dirs("${buildDir}/ko-repo/ko-artifact")
    }
  }

  dependencies {
    classpath("ko-artifact:ko:1.0.0")
  }
}

plugins {
  base
  java
  id("org.densebrain.gradle.ko-generator-plugin")
  `kotlin-dsl`
}

repositories {
  google()
  jcenter()
  mavenCentral()
  maven {
    setUrl("https://dl.bintray.com/kotlin/kotlin-eap")
  }
  mavenLocal()
}

koGenerator {
  packageName = "Script"
}

dependencies {
  "compileOnly"(gradleApi())
  "compileOnly"("com.android.tools.build:gradle:${Versions.Plugins.Android}")
  "runtime"("com.android.tools.build:gradle:${Versions.Plugins.Android}")
  "compile"("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.Plugins.Kotlin}")
  "compile"("org.jetbrains.kotlin:kotlin-frontend-plugin:${Versions.Plugins.KotlinFrontend}")
  "compile"("com.jfrog.bintray.gradle:gradle-bintray-plugin:${Versions.Plugins.Bintray}")

}