import com.android.build.api.dsl.extension.AndroidExtension
import com.android.build.api.dsl.extension.LibraryExtension
import com.android.build.api.sourcesets.AndroidSourceSet
import com.android.build.gradle.BaseExtension
import com.android.builder.model.TestOptions
import com.jfrog.bintray.gradle.BintrayExtension
import java.util.Date

plugins {
  id("idea")
  id("com.android.library")
  kotlin("android")
  kotlin("android.extensions")
  id("maven-publish")
  id("com.jfrog.bintray")
}

val currentVersion = rootProject.extra.get("version") as String
val binTrayKey = rootProject.extra.get("binTrayKey") as String

repositories {
  google()
  jcenter()
  mavenCentral()
//  maven {
//    url = java.net.URI("https://dl.bintray.com/kotlin/kotlin-eap")
//  }
}

android {

  setCompileSdkVersion(Versions.android.compileSdk)

  defaultConfig {
    setMinSdkVersion(Versions.android.minSdk)
    setTargetSdkVersion(Versions.android.targetSdk)

    versionCode = 1
    versionName = "1.0"
  
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    testInstrumentationRunnerArguments(mapOf("clearPackageData" to "true"))
  }
  
  testOptions {
    execution = TestOptions.Execution.ANDROIDX_TEST_ORCHESTRATOR.name
  }

  buildTypes {
    getByName("release") {
      setMinifyEnabled(false)
    }
  }
}


dependencies {
  implementation(project(":kdux-core"))
  implementation(kotlin("stdlib-jdk8"))
  "implementation"("androidx.lifecycle:lifecycle-extensions:${Versions.android.lifecycle}")
  "implementation"("androidx.lifecycle:lifecycle-common-java8:${Versions.android.lifecycle}")
  "implementation"("androidx.appcompat:appcompat:${Versions.android.appCompat}")
  
  "androidTestImplementation"("androidx.test:runner:${Versions.android.test}")
  "androidTestUtil"("androidx.test:orchestrator:${Versions.android.test}")
}


val sourcesJar = tasks.register<Jar>("sourcesJar") {
  classifier = "sources"
  from(project.the<BaseExtension>().sourceSets["main"].java.srcDirs)
}


/**
 * Artifacts (SOURCES)
 */
artifacts {
  add("archives", sourcesJar)
}


/**
 * Publication name to be used between
 * maven-publish
 */
val publicationName = "${project.name}-publication"



/**
 * Configure publish
 */
configure<PublishingExtension> {
  publications.create<MavenPublication>(publicationName) {
    groupId = "org.densebrain"
    artifactId = project.name
    version = project.version as String

    val releaseAar = "${buildDir}/outputs/aar/${project.name}-release.aar"

    artifact(sourcesJar.get())
    artifact(releaseAar)
  }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
  kotlinOptions.jvmTarget = Versions.jvm.source
}


bintray {
  user = "jonglanz"
  key = binTrayKey
  setPublications(publicationName)
  pkg(delegateClosureOf<BintrayExtension.PackageConfig> {
    repo = "oss"
    name = project.name
    userOrg = "densebrain"
    setLicenses("Apache-2.0")
    vcsUrl = "https://github.com/jglanz/kdux.git"
    setVersion(VersionConfig().apply {
      released = Date().toString()
      name = currentVersion
    })
  })
}
