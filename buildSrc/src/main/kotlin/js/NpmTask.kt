package js


import org.apache.tools.ant.taskdefs.ExecTask
import org.apache.tools.ant.types.Commandline
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.jetbrains.kotlin.gradle.frontend.util.nodePath
import java.io.File

open class NpmTask : DefaultTask() {

  init {
    dependsOn("assemble")
  }

  @Input
  var workingDirectory: File = project.buildDir

  @Input
  var args:List<String> = emptyList()

  @TaskAction
  fun execute() {
    val execTask = ExecTask().apply {
      setDir(workingDirectory)

      val cmd = Commandline()
      cmd.executable = nodePath(this@NpmTask.project,"npm").first().absolutePath
      cmd.addArguments(args.toTypedArray())
      setCommand(cmd)
    }

    execTask.execute()
  }
}