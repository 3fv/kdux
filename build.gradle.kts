import java.io.File
import com.github.zafarkhaja.semver.Version

buildscript {
  addRepositories(repositories)

  dependencies {
    classpath(libs.semver)
  }
}

plugins {
  alias(libs.plugins.kotlin.jvm) apply false
  alias(libs.plugins.kotlin.kapt) apply false
  alias(libs.plugins.android.application) apply false
  alias(libs.plugins.android.library) apply false
  alias(libs.plugins.kotlin.android) apply false
  alias(libs.plugins.kotlin.compose) apply false
}


interface InjectedOps {
  @get:Inject val execOps: ExecOperations
}
afterEvaluate {
  val versionPatch by tasks.registering {
    val ops = project.objects.newInstance<InjectedOps>()
    val execOps: ExecOperations = ops.execOps

    doLast {
      val versionFile = File("${rootDir}/version.txt")
      val oldVersion = kduxVersion
      val oldVersionCode = kduxVersionCode

      val version = Version.valueOf(oldVersion)
      val newVersion = version.incrementPatchVersion().toString()
      val newVersionCode = oldVersionCode + 1
      versionFile.writeText("${newVersion}\n${newVersionCode}")

      execOps.exec {
        commandLine = listOf("git","add","version.txt")
      }

      execOps.exec {
        commandLine = listOf("git","commit","-m",newVersion)
      }

      execOps.exec {
        commandLine = listOf("git","tag",newVersion)
      }

      execOps.exec {
        commandLine = listOf("git","push","--all")
      }

      execOps.exec {
        commandLine = listOf("git","push","--tags")
      }
    }
  }

  tasks.create("release") {
    dependsOn(versionPatch)
    val ops = project.objects.newInstance<InjectedOps>()
    val execOps: ExecOperations = ops.execOps

    doLast {
      // inside build.gradle.kts or a plugin:


      execOps.exec {
        commandLine = listOf("./gradlew", "publishAll")
      }
    }
  }


//  val publishTasks = publishedProjects.map { name ->
//    project(name).tasks.getByName("publish")
//  }
//
//
//  tasks.create("publishAll") {
//    dependsOn(publishTasks)
//  }
}