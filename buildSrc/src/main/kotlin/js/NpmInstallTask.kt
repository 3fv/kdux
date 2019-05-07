package js

import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.jetbrains.kotlin.gradle.frontend.npm.NpmInstallTask as FrontendNpmInstallTask
import java.io.File

@CacheableTask
open class NpmInstallTask : FrontendNpmInstallTask() {

  @OutputFile
  val cachedPackageFile = File(project.buildDir,"package.json.cache")

  private val cachedPackageJson by lazy {
    if (cachedPackageFile.exists()) cachedPackageFile.readText() else ""
  }

  @TaskAction
  fun run() {
    val packageJsonFile = File(project.buildDir,"package.json")
    val packageJson = packageJsonFile.readText()
    val packageJsonMatch = cachedPackageJson == packageJson
    logger.quiet("Package JSON changed: ${!packageJsonMatch}")
    if (packageJsonMatch)
      return

    processInstallation()

    cachedPackageFile.writeText(packageJson)
  }
}