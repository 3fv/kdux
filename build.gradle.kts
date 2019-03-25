// Top-level build file where you can add configuration options common to all sub-projects/modules.


buildscript {

  extra.set("version", File(rootProject.projectDir,"version.txt").readText())
  extra.set("binTrayKey", System.getenv("BINTRAY_API_KEY") ?: "")

  repositories {
    google()
    jcenter()
    mavenCentral()
    maven {
      url = java.net.URI("https://dl.bintray.com/kotlin/kotlin-eap")
    }
  }

  dependencies {
    classpath("com.android.tools.build:gradle:${Versions.android.gradle}")
    classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}")
    classpath("com.jfrog.bintray.gradle:gradle-bintray-plugin:${Versions.bintray}")
  }
}

//  configurations.all {
//    resolutionStrategy {
//      exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib-jre7")
//      exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib-jre8")
//      exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib-jdk7")
//      exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib-jdk7")
//
//      force(
//        "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}",
//        "org.jetbrains.kotlin:kotlin-stdlib-jre7:${Versions.kotlin}",
//        "org.jetbrains.kotlin:kotlin-stdlib-jre8:${Versions.kotlin}",
//        "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}",
//        "org.jetbrains.kotlin:kotlin-reflect:${Versions.kotlin}",
//
//        "androidx.test:core:${Versions.android.testCore}",
//
//        // AndroidJUnitRunner and JUnit Rules
//        "androidx.test:runner:${Versions.android.test}",
//        "androidx.test:rules:${Versions.android.test}",
//
//        // Assertions
//        "androidx.test.ext:junit:${Versions.android.testExt}",
//        "androidx.test.ext:truth:${Versions.android.testExt}",
//        "com.google.truth:truth:${Versions.jvm.googleTruth}",
//
//        // Espresso dependencies
//        "androidx.test.espresso:espresso-core:${Versions.android.testEspresso}",
//        "androidx.test.espresso:espresso-contrib:${Versions.android.testEspresso}",
//        "androidx.test.espresso:espresso-intents:${Versions.android.testEspresso}",
//        "androidx.test.espresso:espresso-accessibility:${Versions.android.testEspresso}",
//        "androidx.test.espresso:espresso-web:${Versions.android.testEspresso}",
//        "androidx.test.espresso.idling:idling-concurrent:${Versions.android.testEspresso}",
//
//        "androidx.test.espresso:espresso-idling-resource:${Versions.android.testEspresso}"
//      )
//    }
//  }
//
//
//  // PUBLISHING
//  if (publishProjects.contains(currentProject.name)) {
//
//    //build.dependsOn(build)
//
////    val binTrayUpload = tasks.getByName<BintrayUploadTask>("binTrayUpload")
////    publishAll.dependsOn(binTrayUpload)
////    publishAllLocal.dependsOn(tasks.getByName("publishToMavenLocal"))
////    installAllLocal.dependsOn("install")
//  }
//
//  // REPOS
//  repositories {
//    google()
//    jcenter()
//    mavenCentral()
//  }
//
//  dependencies {
//    "implementation"("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}")
//    "implementation"("org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}")
//    "implementation"("org.jetbrains.kotlin:kotlin-reflect:${Versions.kotlin}")
//
//    "implementation"("io.arrow-kt:arrow-core:${Versions.jvm.arrow}")
//    "implementation"("io.arrow-kt:arrow-typeclasses:${Versions.jvm.arrow}")
//    "implementation"("io.arrow-kt:arrow-data:${Versions.jvm.arrow}")
//    "implementation"("io.arrow-kt:arrow-syntax:${Versions.jvm.arrow}")
//    "implementation"("io.arrow-kt:arrow-effects-kotlinx-coroutines:${Versions.jvm.arrow}")
//    "implementation"("org.slf4j:slf4j-api:${Versions.jvm.slf4j}")
//
//    //testImplementation("junit:junit:${junitVersion}")
//  }
//}
//
//
