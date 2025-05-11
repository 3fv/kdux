plugins {
  id("com.android.library")
  kotlin("android")
  kotlin("android.extensions")
  `kdux-android-publish`
  `kdux-publish`
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

dependencies {
  implementation(project(":kdux-core"))
  "implementation"(Deps.kotlin.stdlib.jvm)
  listOf(
    Deps.kotlin.coroutines.common,
    Deps.kotlin.coroutines.jvm,
    *Deps.android.lifecycle
  ).forEach {
    "implementation"(it)
  }

  Deps.android.test.framework.forEach { "androidTestImplementation"(it) }
  "androidTestUtil"(Deps.android.test.orchestrator)

}

android {
  setCompileSdkVersion(Versions.android.compileSdk)

  defaultConfig {
    setMinSdkVersion(Versions.android.minSdk)
    setTargetSdkVersion(Versions.android.targetSdk)
    versionCode = kduxVersionCode
    versionName = kduxVersion
//      testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

  }

  buildTypes {
    getByName("release") {
      isMinifyEnabled = false
      setProguardFiles(listOf(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro"))
    }
  }

}


