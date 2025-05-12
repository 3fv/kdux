import com.jfrog.bintray.gradle.BintrayExtension

plugins {
  id("com.jfrog.bintray")
}

//afterEvaluate {
//  configure<BintrayExtension> {
//    user = "jonglanz"
//    key = binTrayKey
//    publish = true
//    override = true
//    setPublications(*uploadPublications.toTypedArray())
//    pkg(delegateClosureOf<BintrayExtension.PackageConfig> {
//      repo = "oss"
//      name = project.name
//      userOrg = "densebrain"
//      setLicenses("Apache-2.0")
//      vcsUrl = "https://github.com/jglanz/kdux.git"
//      setVersion(VersionConfig().apply {
//        released = java.util.Date().toString()
//        name = kduxVersion
//      })
//    })
//  }
//  tasks {
//    val uploadTask = getByName("bintrayUpload")
//    val publishTask = getByName("publish")
//
//    val assembleTask = assembleTask
//    if (assembleTask != null) {
//      uploadTask.mustRunAfter(assembleTask)
//      publishTask.mustRunAfter(assembleTask)
//    }
//
//    publishTask.finalizedBy(uploadTask)
//  }
//
//
//}