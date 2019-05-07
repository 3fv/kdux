import java.io.File
import com.github.zafarkhaja.semver.Version

buildscript {
  addRepositories(repositories)

  dependencies {
    classpath(Deps.gradle.semver)
  }
}

afterEvaluate {
  val versionPatch by tasks.registering {
    doLast {
      val versionFile = File("${rootDir}/version.txt")
      val oldVersion = kduxVersion
      val oldVersionCode = kduxVersionCode
      
      val version = Version.valueOf(oldVersion)
      val newVersion = version.incrementPatchVersion().toString()
      val newVersionCode = oldVersionCode + 1
      versionFile.writeText("${newVersion}\n${newVersionCode}")

      exec {
        commandLine = listOf("git","add","version.txt")
      }

      exec {
        commandLine = listOf("git","commit","-m",newVersion)
      }

      exec {
        commandLine = listOf("git","tag",newVersion)
      }

      exec {
        commandLine = listOf("git","push","--all")
      }

      exec {
        commandLine = listOf("git","push","--tags")
      }
    }
  }

  tasks.create("release") {
    dependsOn(versionPatch)

    doLast {
      exec {
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