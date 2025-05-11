import com.jfrog.bintray.gradle.BintrayExtension
import org.gradle.api.publish.maven.internal.publication.DefaultMavenPublication
import java.util.Date

plugins {
//  id("idea")
  id("java")
  kotlin("jvm")
  id("java-library")
  `maven-publish`
  `kdux-publish`
}

addRepositories(repositories)

dependencies {
  implementation(kotlin("stdlib", Versions.Plugins.Kotlin))
  implementation(Deps.kotlin.coroutines.jvm)
  implementation(Deps.jvm.slf4j)
  implementation(kotlin("reflect", Versions.Plugins.Kotlin))
  implementation(kotlin("stdlib-common", Versions.Plugins.Kotlin))
  implementation(Deps.kotlin.coroutines.common)
  testImplementation(kotlin("test-junit5", Versions.Plugins.Kotlin))
  testImplementation(kotlin("test-common", Versions.Plugins.Kotlin))
  testImplementation(kotlin("test-annotations-common", Versions.Plugins.Kotlin))
}


/**
 * Sources JAR for distro
 */
//val sourcesJar = tasks.register<Jar>("sourceJar") {
//  classifier = "sources"
//  from(
//    sourceSets["main"].java.srcDirs
////    *kotlin.sourceSets.map { sourceSet -> sourceSet.kotlin.srcDirs }.toTypedArray()
//  )
//}

/**
 * Publication name to be used between
 * maven-publish
 */
publishing {
  repositories {
    clear()
  }

  publications.all { pub ->
    val publication = pub as MavenPublication
    val type = publication.name
    publication.groupId = "org.densebrain"
    publication.artifactId = when (type) {
      "metadata" -> "${name}-metadata"
      "jvm" -> "${name}-jvm"
      else -> name
    }

    //publication.artifact(sourcesJar.get())
    publication.version = kduxVersion
    uploadPublications.add(type)
    true
  }
}

