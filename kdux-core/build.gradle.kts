
plugins {
  alias(libs.plugins.kotlin.kapt)
  alias(libs.plugins.kotlin.jvm)
  `java-library`
  `maven-publish`
}
val ghCreds = githubCredentials
val ghUser: String? = ghCreds.first
val ghToken: String? = ghCreds.second
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
java {
  withSourcesJar()
}

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

      from(components["java"])

    }
  }
}