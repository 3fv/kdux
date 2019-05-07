import com.android.build.gradle.BaseExtension
import com.android.build.api.dsl.extension.LibraryExtension
import com.android.build.api.dsl.model.BuildType
import com.android.build.gradle.TestedExtension
import com.android.build.gradle.tasks.BundleAar
//import com.android.build.gradle.TestedExtension
//import com.android.build.gradle.LibraryExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.*
import java.net.URI
import Versions
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.bundling.Jar

fun androidProjectSetup(project: Project) = with(project) {
  val kotlinVersion = Versions.Plugins.Kotlin
  logger.quiet("Kotlin version: ${kotlinVersion}")
  addRepositories(repositories)




  configure<TestedExtension> {
    setCompileSdkVersion(Versions.android.compileSdk)

    useLibrary("android.test.runner")
    useLibrary("android.test.base")
    useLibrary("android.test.mock")

    defaultConfig {
      setMinSdkVersion(Versions.android.minSdk)
      setTargetSdkVersion(Versions.android.targetSdk)
      versionCode = kduxVersionCode// kduxVersion.split(".").last().toInt()
      versionName = kduxVersion //.split(".").subList(0,2).joinToString(".")
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

    packagingOptions {
      pickFirst("lib/**/*.so")
    }

    afterEvaluate {
      tasks.withType<BundleAar> {
        archiveVersion.set(kduxVersion)
      }
    }
  }

  configurations.all {
    resolutionStrategy {
      force(
        Deps.jvm.findBugs
      )
    }
  }
  dependencies {


    "implementation"(Deps.kotlin.stdlib.jvm)
    "implementation"(kduxCoreProject) {
      setTransitive(true)
    }

    listOf(
      Deps.kotlin.coroutines.common,
      Deps.kotlin.coroutines.jvm,
      *Deps.android.lifecycle
    ).forEach {
      "implementation"(it)
    }

    "androidTestUtil"(Deps.android.test.orchestrator)

    // Core library

    Deps.android.test.framework.forEach { "androidTestImplementation"(it) }

  }

}

fun androidProjectPublishSetup(project: Project) = with(project) {

  val sourcesJar = tasks.register<Jar>("sourcesJar") {
    archiveClassifier.set("sources")
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

  uploadPublications.add(defaultPublicationName)


  afterEvaluate {
    /**
     * Configure publish
     */
    configure<PublishingExtension> {
      publications.create<MavenPublication>(defaultPublicationName) {
        groupId = "org.densebrain"
        artifactId = project.name
        version = project.kduxVersion

        logger.quiet("Release AAR: ${androidLibraryReleaseAar.get().asFile.absolutePath}")
        artifact(sourcesJar.get())
        artifact(androidLibraryReleaseAar)
      }
    }
  }
  tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = Versions.jvm.source
  }
}