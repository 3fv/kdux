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
  @get:Inject
  val execOps: ExecOperations
}

afterEvaluate {

  // EXPLICITLY SET PUBLISH TO DEPEND ON `ProjectHelpers.Project.assembleTask`
  publishedProjects.forEach { pubName ->
    val pubProject = project(pubName)
    pubProject.afterEvaluate {
      pubProject.tasks {
        val publishTask = getByName("publish")
        val assembleTask = assembleTask
        if (assembleTask != null) {
          publishTask.mustRunAfter(assembleTask)
        }
      }
    }
  }

  /**
   * This task will increment the patch version of the project and commit the changes to git.
   */
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
        commandLine = listOf("git", "add", "version.txt")
      }

      execOps.exec {
        commandLine = listOf("git", "commit", "-m", newVersion)
      }

      execOps.exec {
        commandLine = listOf("git", "tag", newVersion)
      }

      execOps.exec {
        commandLine = listOf("git", "push", "--all")
      }

      execOps.exec {
        commandLine = listOf("git", "push", "--tags")
      }
    }
  }

  /**
   * This task will publish the project to the github.com/3fv/kdux maven repo.
   */
  tasks.create("release") {
    dependsOn(versionPatch)
    val ops = project.objects.newInstance<InjectedOps>()
    val execOps: ExecOperations = ops.execOps

    doLast {
      execOps.exec {
        commandLine = listOf("./gradlew", "publish")
      }
    }
  }

}

