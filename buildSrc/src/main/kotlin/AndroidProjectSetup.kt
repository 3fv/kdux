import com.android.build.gradle.TestedExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.*
import java.net.URI

fun androidProjectSetup(project: Project) = with(project) {
  repositories {
    google()
    jcenter()
    mavenCentral()
    maven {
      url = URI("https://dl.bintray.com/kotlin/kotlin-eap")
    }
    mavenLocal()
  }



  configure<TestedExtension> {
    setCompileSdkVersion(Versions.android.compileSdk)

    useLibrary("android.test.runner")
    useLibrary("android.test.base")
    useLibrary("android.test.mock")

    defaultConfig {
      setMinSdkVersion(Versions.android.minSdk)
      setTargetSdkVersion(Versions.android.targetSdk)
      versionCode = kduxVersionCode
      versionName = kduxVersion
      testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    }
    buildTypes {
      getByName("release") {
        isMinifyEnabled = false
        setProguardFiles(listOf(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro"))
      }
    }

    testOptions {
      execution = "ANDROIDX_TEST_ORCHESTRATOR"
    }
  }

  dependencies {

    "implementation"(project(":kdux-core")) {
      setTransitive(true)
    }
    "implementation"(kotlin("stdlib-jdk8"))


    "implementation"(kduxCoreProject)


    listOf(
      "androidx.lifecycle:lifecycle-extensions:${Versions.android.lifecycle}",
      "androidx.lifecycle:lifecycle-common-java8:${Versions.android.lifecycle}",
      "androidx.appcompat:appcompat:${Versions.android.appCompat}"
    ).forEach {
      "implementation"(it)
    }

    "androidTestUtil"("androidx.test:orchestrator:${Versions.android.test}")

    // Core library
    listOf(
      "androidx.test:core:${Versions.android.testCore}",

      // AndroidJUnitRunner and JUnit Rules
      "androidx.test:runner:${Versions.android.test}",
      "androidx.test:rules:${Versions.android.test}",

      // Assertions
      "androidx.test.ext:junit:${Versions.android.testExt}",
      "androidx.test.ext:truth:${Versions.android.testExt}",
      "com.google.truth:truth:${Versions.jvm.googleTruth}",

      // Espresso dependencies
      "androidx.test.espresso:espresso-core:${Versions.android.testEspresso}",
      "androidx.test.espresso:espresso-contrib:${Versions.android.testEspresso}",
      "androidx.test.espresso:espresso-intents:${Versions.android.testEspresso}",
      "androidx.test.espresso:espresso-accessibility:${Versions.android.testEspresso}",
      "androidx.test.espresso:espresso-web:${Versions.android.testEspresso}",
      "androidx.test.espresso.idling:idling-concurrent:${Versions.android.testEspresso}",


      "androidx.test.espresso:espresso-idling-resource:${Versions.android.testEspresso}"
    ).forEach { "androidTestImplementation"(it) }

  }

}