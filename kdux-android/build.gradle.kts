import com.android.build.gradle.BaseExtension
import com.android.build.gradle.tasks.BundleAar
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getByName

plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)
  `maven-publish`
}

val ghCreds = githubCredentials
val ghUser: String? = ghCreds.first
val ghToken: String? = ghCreds.second

dependencies {
  implementation(project(":kdux-core"))
  implementation(libs.kotlin.stdlib)
  implementation(libs.kotlin.coroutines.core)
  implementation(libs.kotlin.coroutines.core.jvm)

  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation(libs.androidx.lifecycle.process)
  implementation(libs.androidx.appcompat)
  implementation(libs.androidx.lifecycle.livedata.ktx)
  implementation(libs.androidx.lifecycle.viewmodel.ktx)

  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso.core)

}

android {
  namespace = "org.tfv.kdux.android"
  buildToolsVersion = "35.0.0"
  compileSdk = 35

  defaultConfig {
    minSdk = 31
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

val sourcesJar = tasks.register<Jar>("sourcesJar") {
  archiveClassifier.set("sources")
  from(project.the<BaseExtension>().sourceSets["main"].java.srcDirs)
}

artifacts {
  add("archives", sourcesJar)
}

afterEvaluate {
  configure<PublishingExtension> {
    repositories {
      maven {
        group = "org.tfv"
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/3fv/kdux")
        credentials {
          username = ghUser
          password = ghToken
        }
      }
    }
    publications {
      register<MavenPublication>("gpr") {
        groupId = "org.tfv"
        artifactId = project.name
        version = kduxVersion

        val aarFile = tasks.getByName<BundleAar>("bundleReleaseAar").archiveFile
        logger.quiet("Release AAR: ${aarFile.get().asFile.absolutePath}")
        artifact(sourcesJar.get())
        artifact(aarFile)
      }
    }
  }
}