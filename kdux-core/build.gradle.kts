import com.jfrog.bintray.gradle.BintrayExtension
import org.gradle.api.publish.maven.internal.publication.DefaultMavenPublication
import java.util.Date

plugins {
  alias(libs.plugins.kotlin.kapt)
  alias(libs.plugins.kotlin.jvm)
//  kotlin("jvm") version libs.versions.kotlin
  `java-library`
//  `maven-publish`
//  `kdux-publish`
}

dependencies {
  implementation(libs.kotlin.stdlib)
  implementation(libs.kotlin.reflect)
  implementation(libs.kotlin.coroutines.core)
  implementation(libs.kotlin.coroutines.core.jvm)

//  implementation(libs.slf4j.simple)
  implementation(libs.slf4j.api)
  testImplementation(libs.kotlin.test)
  testImplementation(libs.junit)
  testImplementation(libs.junit.jupiter)
  testImplementation(libs.kotlin.test.annotations.common)
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
//publishing {
//  repositories {
//    clear()
//  }
//
//  publications.all { pub ->
//    val publication = pub as MavenPublication
//    val type = publication.name
//    publication.groupId = "org.densebrain"
//    publication.artifactId = when (type) {
//      "metadata" -> "${name}-metadata"
//      "jvm" -> "${name}-jvm"
//      else -> name
//    }
//
//    //publication.artifact(sourcesJar.get())
//    publication.version = kduxVersion
//    uploadPublications.add(type)
//    true
//  }
//}
//
