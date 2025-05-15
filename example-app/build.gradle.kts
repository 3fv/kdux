plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.kotlin.kapt)
}

android {
  namespace = "org.tfv.kdux.example"
  buildToolsVersion = "35.0.0"
  compileSdk = 35

  useLibrary("android.test.runner")
  useLibrary("android.test.base")
  useLibrary("android.test.mock")

  defaultConfig {
    applicationId = "org.tfv.kdux.example"

    minSdk = 31
    targetSdk = 35
    versionCode = kduxVersionCode
    versionName = kduxVersion
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }
  buildFeatures {
    buildConfig = true
    compose = true
    viewBinding = true
    dataBinding = true
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
  dependenciesInfo {
    includeInApk = true
    includeInBundle = true
  }

}

dependencies {

  implementation(kduxCoreProject)
  implementation(kduxAndroidProject) {
    isTransitive = true
  }
  implementation(libs.kotlin.stdlib)
  implementation(libs.kotlin.coroutines.core)
  implementation(libs.kotlin.coroutines.core.jvm)

  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.lifecycle.process)
  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation(libs.androidx.activity.compose)
  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.androidx.ui)
  implementation(libs.androidx.ui.graphics)
  implementation(libs.androidx.ui.tooling.preview)
  implementation(libs.androidx.material3)
  implementation(libs.androidx.appcompat)
  implementation(libs.material)
  implementation(libs.androidx.constraintlayout)
  implementation(libs.androidx.lifecycle.livedata.ktx)
  implementation(libs.androidx.lifecycle.viewmodel.ktx)
  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.test.rules)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso.core)
  androidTestImplementation(platform(libs.androidx.compose.bom))
  androidTestImplementation(libs.androidx.ui.test.junit4)
  debugImplementation(libs.androidx.ui.tooling)
  debugImplementation(libs.androidx.ui.test.manifest)
}