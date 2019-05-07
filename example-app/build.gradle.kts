import com.android.build.gradle.AppExtension
import java.net.URI

plugins {
  id("idea")
  id("com.android.application")
  kotlin("android") // version(Versions.plugins.kotlin)
  kotlin("android.extensions") // version(Versions.plugins.kotlin)
}

androidProjectSetup(project)

configure<AppExtension> {
	defaultConfig {
		applicationId = "org.densebrain.kdux.example"
	}

	dataBinding {
		isEnabled = true
	}
}

repositories {
  maven { url = URI("https://jitpack.io") }
}

dependencies {
	"implementation"(Deps.android.fb.soLoader)
	"implementation"(Deps.android.fb.flipper) {
    isTransitive = true
  }
  "implementation"(kduxAndroidFlipperPluginProject) {
    isTransitive = true
  }
	"implementation"(kduxAndroidProject) {
    isTransitive = true
  }
}