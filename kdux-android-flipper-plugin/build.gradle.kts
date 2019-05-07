
plugins {
  id("idea")
  id("com.android.library")
  kotlin("android") // version(Versions.plugins.kotlin)
  kotlin("android.extensions") // version(Versions.plugins.kotlin)
  `kdux-android-publish`
  `kdux-publish`
}

dependencies {
  implementation(Deps.android.fb.soLoader)
  implementation(Deps.android.fb.flipper) {
    isChanging = true
    isTransitive = true
  }
  implementation(Deps.jvm.gson)
  implementation(Deps.kotlin.reflect.jvm)
  implementation(kduxAndroidProject)
}