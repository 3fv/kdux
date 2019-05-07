//import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.the

plugins {
  id("maven-publish")
}

androidProjectSetup(project)
androidProjectPublishSetup(project)




