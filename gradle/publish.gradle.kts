//import com.jfrog.bintray.gradle.BintrayExtension

//buildscript {
//
//  extra.set("binTrayKey", System.getenv("BINTRAY_API_KEY") ?: "")
//
//  repositories {
//    google()
//    jcenter()
//    mavenCentral()
//  }
//
//  dependencies {
//    classpath("com.jfrog.bintray.gradle:gradle-bintray-plugin:${Versions.bintray}")
//  }
//}
//
//
//plugins {
//  id("com.jfrog.bintray")
//}
//
//val version = extra.get("version") as String
//val binTrayKey = extra.get("binTrayKey") as String

//
//val sourceJar = tasks.create("sourceJar",Jar::class.java) {
//  from(sourceSets.main.allSource)
//}
//
//publishing {
//  publications {
//    mavenJava(MavenPublication) {
//      groupId 'org.densebrain'
//      artifactId = project.name
//        version = version
//        from = components.java
//
////        artifact sourceJar {
////        classifier "sources"
////      }
//    }
//  }
//}

///**
// * Sources JAR for distro
// */
//val sourcesJar = tasks.create<Jar>("sourceJar") {
//  classifier = "sources"
//  from(sourceSets["main"].java.srcDirs)
//}
//
///**
// * Artifacts (SOURCES)
// */
//artifacts {
//  add("archives", sourcesJar)
//}
//
//
///**
// * Publication name to be used between
// * maven-publish
// */
//val publicationName = "${project.name}-publication"
//
///**
// * Configure publish
// */
//configure<PublishingExtension> {
//  publications.create<MavenPublication>(publicationName) {
//    from(components["java"])
//    groupId = "org.densebrain"
//    artifactId = project.name
//    version = project.version as String
//    artifact(sourcesJar)
//  }
//}
//
//bintray {
//  user = "jonglanz"
//  key = binTrayKey
//  setPublications("mavenJava")
//  pkg(delegateClosureOf<BintrayExtension.PackageConfig> {
//    repo = "oss"
//    name = project.name
//    userOrg = "densebrain"
//    setLicenses("Apache-2.0")
//    vcsUrl = "https://github.com/jglanz/kdux.git"
////        setVersion
////        version {
////          name = version
////          released = new Date ()
////        }
//  })
//}

