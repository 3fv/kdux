import java.io.File
import java.nio.file.Files

plugins {
  //id("com.moowork.node") version(Versions.Plugins.Node)
  //id("org.jetbrains.kotlin.frontend") version (Versions.plugins.kotlinFrontend)
}

val nodeModulesDir = File("${rootDir}/node_modules").apply { mkdirs() }

val setup = tasks.create("setup") {
   doLast {
     val flipperDir = File((properties["flipper.path"] as? String?) ?: "/projects/oss/flipper")
     if (!flipperDir.exists())
       error("flipper.path is either not set or incorrect (from gradle.properties or local.properties)")


     val createLink = { from:File, to:File ->
       if (!to.exists())
        Files.createSymbolicLink(to.toPath(),from.toPath())
     }

     createLink(File(flipperDir,"src"),File(nodeModulesDir,"flipper"))
     File(flipperDir,"node_modules").listFiles { file:File -> !file.startsWith(".") && file.isDirectory }.forEach { dir:File ->
       createLink(dir,File(nodeModulesDir,dir.name))
     }
   }
}


afterEvaluate {
  val build = tasks.findByName("build")
  build?.dependsOn(setup)
}