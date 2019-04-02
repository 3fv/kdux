import com.jfrog.bintray.gradle.BintrayExtension
import js.NpmTask
import org.gradle.api.publish.maven.internal.publication.DefaultMavenPublication
import org.jetbrains.kotlin.gradle.dsl.KotlinJsCompile
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType
import org.jetbrains.kotlin.konan.target.hostTargetSuffix
import java.util.Date

plugins {
  id("idea")
  id("java")
  id("java-library")
  kotlin("multiplatform")
  id("maven-publish")
  id("com.jfrog.bintray")
  id("org.jetbrains.kotlin.frontend")
}


val (currentVersion,binTrayKey) = arrayOf("version","binTrayKey").map {
  rootProject.extra.get(it) as String
}

kotlin {
  targets {
    targetFromPreset(presets.getByName("jvm"), "jvm") {
      compilations["main"].defaultSourceSet {
        dependencies {
          implementation(kotlin("stdlib-jdk8"))
          implementation(Deps.kotlin.coroutines.jvm)
          implementation(Deps.jvm.slf4j)
        }
      }
      // JVM-specific tests and their dependencies:
      compilations["test"].defaultSourceSet {
        dependencies {
          implementation(kotlin("test-junit5"))
        }
      }
    }

    // Creates a JVM target with the default name 'jvm'
    targetFromPreset(presets.getByName("js"), "js") {

      arrayOf("test","main").forEach { compilationName ->
        val suffix = when(compilationName) {
          "test" -> "-tests"
          else -> ""
        }

        configure(listOf(compilations[compilationName])) {
          tasks.getByName<KotlinJsCompile>(compileKotlinTaskName) {
            kotlinOptions {
              metaInfo = true
              sourceMap = true
              sourceMapEmbedSources = "always"
              moduleKind = "commonjs"
              main = "call"
              outputFile = "${project.buildDir.absolutePath}/js${suffix}/${project.name}${suffix}.js"
            }
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
        implementation(Deps.kotlin.coroutines.common)
      }
    }
     
    commonTest {
      dependencies {
        implementation(kotlin("test-common"))
        implementation(kotlin("test-annotations-common"))
      }
    }



    js {

      compilations["main"].defaultSourceSet {
        dependencies {
          implementation(kotlin("stdlib-js"))
          implementation(Deps.kotlin.coroutines.js)
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
    devDependency("source-map-support")
    devDependency("jest")
    devDependency("jest-junit")
  }

//  bundle<WebPackExtension>("webpack") {
//    val webPack = this@bundle as WebPackExtension
//    webPack.bundleName = "main"
//    //webPack.proxyUrl = "http://localhost:8080"
//
//  }



}

fun isAvailableForPublication(publication:MavenPublication):Boolean {
  val name = publication.name
  return arrayOf("jvm","js","metadata","kotlinMultiplatform").contains(name)
}

tasks.withType<AbstractPublishToMaven>().all {
  onlyIf { isAvailableForPublication(publication) }
}

/**
 * Sources JAR for distro
 */
val sourcesJar = tasks.register<Jar>("sourceJar") {
  classifier = "sources"
  from(
    sourceSets["main"].java.srcDirs,
    *kotlin.sourceSets.map { sourceSet -> sourceSet.kotlin.srcDirs }.toTypedArray()
  )
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
//val publicationName = "${project.name}-publication"

/**
 * Configure publish
 */
//configure<PublishingExtension> {
//  publications.create<MavenPublication>(publicationName) {
//    from(components["java"])
//    groupId = "org.densebrain"
//    artifactId = project.name
//    version = project.version as String
//    artifact(sourcesJar)
//
//  }
//}

val uploadPublications = mutableListOf<String>()

publishing {
  repositories {
    mavenLocal()
  }

  publications.all { pub ->
    val publication = pub as MavenPublication
    val type = publication.name
    logger.quiet("Publication: ${name}-${type}")
    publication.groupId = "org.densebrain"
    publication.artifactId = when (type) {
      "metadata" -> "${name}-metadata"
      "jvm" -> "${name}-jvm"
      "js" -> "${name}-js"
      else -> name
    }

    publication.artifact(sourcesJar.get())
    publication.version = currentVersion
    uploadPublications.add(type)
    true
  }

  kotlin.targets.all { target ->
    val publication = publishing.publications.findByName(target.name)

    if (publication != null) {
      if (target.platformType != KotlinPlatformType.native) {
        (publication as DefaultMavenPublication).setModuleDescriptorGenerator(null)
      }
    }

    true
  }
}

bintray {
  user = "jonglanz"
  key = binTrayKey
  publish = true
  setPublications(*uploadPublications.toTypedArray())
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

