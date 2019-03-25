import com.jfrog.bintray.gradle.BintrayExtension
import js.NpmTask
import org.jetbrains.kotlin.gradle.dsl.KotlinJsCompile
import java.util.Date

plugins {
  id("idea")
  id("java")
  id("java-library")
  id("maven-publish")
  kotlin("multiplatform")
  id("com.jfrog.bintray")
  id("org.jetbrains.kotlin.frontend")
}

val currentVersion = rootProject.extra.get("version") as String
val binTrayKey = rootProject.extra.get("binTrayKey") as String

kotlin {

  targets {
    targetFromPreset(presets.getByName("jvm"), "jvm") {} // Creates a JVM target with the default name 'jvm'
    targetFromPreset(presets.getByName("js"), "js") {

      configure(listOf(compilations["main"], compilations["test"])) {
        tasks.getByName<KotlinJsCompile>(compileKotlinTaskName) {
          kotlinOptions {
            metaInfo = true
            sourceMap = true
            //sourceMapPrefix = projectDir.absolutePath
            sourceMapEmbedSources = "always"
            moduleKind = "commonjs"
            main = "call"
          }
        }
      }

      configure(listOf(compilations["test"])) {
        tasks.getByName<KotlinJsCompile>(compileKotlinTaskName) {
          kotlinOptions {
            outputFile = "${project.buildDir.absolutePath}/js-tests/${project.name}-tests.js"
          }
        }
      }

      configure(listOf(compilations["main"])) {
        tasks.getByName<KotlinJsCompile>(compileKotlinTaskName) {
          kotlinOptions {
            outputFile = "${project.buildDir.absolutePath}/js/${project.name}.js"
          }
        }
      }
    }
  }

  sourceSets {
    forEach { sourceSet ->
      sourceSet.kotlin.srcDir("${sourceSet.name}/kotlin")
    }

    commonMain {
      dependencies {
        implementation(kotlin("reflect"))
        implementation(kotlin("stdlib-common"))
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-common:${Versions.kotlinCoroutine}")
      }
    }
     
    commonTest {
      dependencies {
        implementation(kotlin("test-common"))
        implementation(kotlin("test-annotations-common"))
      }
    }


    // Default source set for JVM-specific sources and dependencies:
    jvm {
      compilations["main"].defaultSourceSet {
        dependencies {
          //implementation(kotlin("stdlib"))
          implementation(kotlin("stdlib-jdk8"))
          implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinCoroutine}")
          implementation("org.slf4j:slf4j-api:${Versions.jvm.slf4j}")
        }
      }
      // JVM-specific tests and their dependencies:
      compilations["test"].defaultSourceSet {
        dependencies {
          implementation(kotlin("test-junit5"))
        }
      }
    }

    js {

      compilations["main"].defaultSourceSet {
        dependencies {
          implementation(kotlin("stdlib-js"))
          implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:${Versions.kotlinCoroutine}")
        }
      }
      compilations["test"].defaultSourceSet {
        dependencies {
          implementation(kotlin("test-js"))
        }
      }
    }

    all {
      languageSettings.apply {
        languageVersion = "1.3"
        apiVersion = "1.3"
        enableLanguageFeature("InlineClasses") // language feature name
        useExperimentalAnnotation("kotlin.ExperimentalUnsignedTypes") // annotation FQ-name
        progressiveMode = true
      }
    }
  }
}

kotlinFrontend {
  downloadNodeJsVersion = Versions.js.node
  sourceMaps = true

  npm {
    //devDependency("karma")
    dependency("source-map-support")
    devDependency("jest")
    devDependency("jest-junit")
  }

//  bundle<WebPackExtension>("webpack") {
//    val webPack = this@bundle as WebPackExtension
//    webPack.bundleName = "main"
//    //webPack.proxyUrl = "http://localhost:8080"
//
//  }
//
//  karma {
//    //enableWebPack = true
//  }

}

/**
 * Sources JAR for distro
 */
val sourcesJar = tasks.create<Jar>("sourceJar") {
  classifier = "sources"
  from(sourceSets["main"].java.srcDirs)
}

val jest = tasks.create("jest", NpmTask::class.java) {
  args = listOf("run","test")
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


afterEvaluate {
  tasks.getByName("jsTest").dependsOn(jest)
}

