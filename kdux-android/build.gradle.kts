//buildscript {
//  apply from: "${rootDir}/gradle/versions.gradle"
//}

android {
  compileSdkVersion = Versions.COMPILE_SDK_VERSION
  
  defaultConfig {
    minSdkVersion = Versions.MIN_SDK_VERSION
    targetSdkVersion = Versions.TARGET_SDK_VERSION
    versionCode = 1
    versionName = "1.0"
  
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  
    // The following argument makes the Android Test Orchestrator run its
    // "pm clear" command after each test invocation. This command ensures
    // that the app"s state is completely cleared between tests.
    testInstrumentationRunnerArguments(clearPackageData = "true")
    
  }
  
  testOptions {
    execution = "ANDROIDX_TEST_ORCHESTRATOR"
  }

  buildTypes {
    release {
      minifyEnabled = false
      proguardFiles = getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro"
    }
  }
}



dependencies {
  "implementation"(project(":kdux-core"))
  
  "implementation"("androidx.lifecycle:lifecycle-extensions:${Versions.android.lifecycle}")
  "implementation"("androidx.lifecycle:lifecycle-common-java8:${Versions.android.lifecycle}")
  "implementation"("androidx.appcompat:appcompat:${Versions.android.appCompat}")
  
  "androidTestImplementation"("androidx.test:runner:${Versions.android.test}")
  "androidTestUtil"("androidx.test:orchestrator:${Versions.android.test}")
}

val sourcesJar = tasks.create("sourceJar",Jar::class.java) {
  classifier = "sources"
  from(android.sourceSets.main.java.srcDirs)
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
    from(components["java"])
    groupId = "org.densebrain"
    artifactId = project.name
    version = project.version as String
    artifact(sourcesJar)
  }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
  kotlinOptions.jvmTarget = Versions.kotlinJVM
}

//val publicationName = "${project.name}-publication"
//
//publishing {
//  publications {
//    mavenJava(MavenPublication) {
//      groupId "org.densebrain"
//      artifactId project.name
//      version VERSION
//
//      artifact bundleReleaseAar
//      artifact sourceJar {
//        classifier "sources"
//      }
//    }
//  }
//}
