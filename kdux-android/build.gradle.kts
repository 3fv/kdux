
plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)
//  `kdux-android-publish`
//  `kdux-publish`
}


dependencies {
  implementation(project(":kdux-core"))
  implementation(libs.kotlin.stdlib)
  implementation(libs.kotlin.coroutines.core)
  implementation(libs.kotlin.coroutines.core.jvm)

  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation(libs.androidx.lifecycle.process)
//  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.androidx.appcompat)
  implementation(libs.androidx.lifecycle.livedata.ktx)
  implementation(libs.androidx.lifecycle.viewmodel.ktx)

  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso.core)
//  androidTestImplementation(platform(libs.androidx.compose.bom))
//  androidTestImplementation(libs.androidx.ui.test.junit4)
//  debugImplementation(libs.androidx.ui.tooling)
//  debugImplementation(libs.androidx.ui.test.manifest)

}

android {
  namespace = "org.densebrain.kdux.android"
  buildToolsVersion = "35.0.0"
  compileSdk = 35

  defaultConfig {
    minSdk = 31

//    versionCode = kduxVersionCode
//    versionName = kduxVersion
//      testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

  }

  buildTypes {
    getByName("release") {
      isMinifyEnabled = false
      setProguardFiles(listOf(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro"))
    }
  }

  buildFeatures {
    buildConfig = true
  }

  buildTypes {
    debug {
      isMinifyEnabled = false
      buildConfigField("boolean", "DEBUG", "true")
    }
    release {
      isMinifyEnabled = false
      proguardFiles(
        getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro"
      )
      buildConfigField("boolean", "DEBUG", "false")
    }
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }

  kotlinOptions {
    jvmTarget = "17"
  }
}


