import com.android.build.gradle.AppExtension
import com.android.build.gradle.TestedExtension
import org.gradle.kotlin.dsl.*

import Versions

plugins {
  id("com.android.application")
  kotlin("android")
  kotlin("android.extensions")
}

repositories {
  google()
  gradlePluginPortal()
  jcenter()
  mavenCentral()
  maven { url = uri("https://jitpack.io") }
  maven {
    url = uri("https://dl.bintray.com/kotlin/kotlin-eap")
  }
}

android {
  setCompileSdkVersion(Versions.android.compileSdk)

  defaultConfig {
    applicationId = "org.densebrain.kdux.example"
    setMinSdkVersion(Versions.android.minSdk)
    setTargetSdkVersion(Versions.android.targetSdk)
    versionCode = kduxVersionCode
    versionName = kduxVersion


  }


  dataBinding {
		isEnabled = true
	}

}

dependencies {

	"implementation"(Deps.android.fb.soLoader)
  "implementation"(kduxCoreProject)
	"implementation"(kduxAndroidProject) {
    isTransitive = true
  }

  listOf(
    Deps.kotlin.coroutines.common,
    Deps.kotlin.coroutines.jvm,
    *Deps.android.lifecycle
  ).forEach {
    "implementation"(it)
  }
}