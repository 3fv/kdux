import Script.Versions as ScriptVersions


object Versions {
  val Plugins = ScriptVersions.Plugins

  val kotlinCoroutine = "1.1.1"

  object js {
    val node = "10.5.0"
  }

  object jvm {
    val source         = "1.8"
    val googleTruth            = "0.42"
    val arrow                  = "0.7.3"
    val slf4j                  = "1.7.25"
    val googleAuto             = "1.0-rc4"
    val junit                  = "4.12"
    val gson                   = "2.8.5"
    val semver                 = "0.9.0"
    val findBugs               = "3.0.1"
  }

  object android {
    val minSdk = 24
    val targetSdk = 28
    val compileSdk = 28

    val fbSoLoader      = "0.5.1"
    val fbFlipper       = "2.0.0-DENSEBRAIN-03"
    val anko            = "0.10.5"

    val support         = "28.0.0"
    val test            = "1.1.1"
    val testCore        = "1.0.0"
    val testExt         = "1.0.0"
    val testEspresso    = "3.1.0"
    val testRunner      = "1.0.2"
    val constraintLayout= "1.0.2"
    val lifecycle       = "2.0.0"
    val appCompat       = "1.0.2"

  }
}

object Deps {

  object gradle {
    val node = "com.moowork.gradle:gradle-node-plugin:${Versions.Plugins.Node}"
    val semver = "com.github.zafarkhaja:java-semver:${Versions.jvm.semver}"
  }

  object kotlin {
    object coroutines {
      val common = "org.jetbrains.kotlinx:kotlinx-coroutines-core-common:${Versions.kotlinCoroutine}"
      val jvm = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinCoroutine}"
      val js = "org.jetbrains.kotlinx:kotlinx-coroutines-core-js:${Versions.kotlinCoroutine}"
    }

    object reflect {
      val jvm = "org.jetbrains.kotlin:kotlin-reflect:${Versions.Plugins.Kotlin}"
    }

    object stdlib {
      val jvm = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.Plugins.Kotlin}"
    }
  }


  object jvm {
    val slf4j = "org.slf4j:slf4j-api:${Versions.jvm.slf4j}"
    val findBugs = "com.google.code.findbugs:jsr305:${Versions.jvm.findBugs}"
    val gson = "com.google.code.gson:gson:${Versions.jvm.gson}"
  }

  object android {
    object fb {
      val flipperGroup = "com.facebook.flipper"
      val flipperPrefix = "${flipperGroup}:flipper"
      val fbjniPrefix = "${flipperGroup}:fbjni"
      val flipper = "${flipperPrefix}:${Versions.android.fbFlipper}"
      val soLoader = "com.facebook.soloader:soloader:${Versions.android.fbSoLoader}"
    }

    val lifecycle = arrayOf(
      "androidx.lifecycle:lifecycle-extensions:${Versions.android.lifecycle}",
      "androidx.lifecycle:lifecycle-common-java8:${Versions.android.lifecycle}",
      "androidx.appcompat:appcompat:${Versions.android.appCompat}"
    )

    object test {
      val orchestrator = "androidx.test:orchestrator:${Versions.android.test}"
      val framework = arrayOf(
        "androidx.test:core:${Versions.android.testCore}",

        // AndroidJUnitRunner and JUnit Rules
        "androidx.test:runner:${Versions.android.test}",
        "androidx.test:rules:${Versions.android.test}",

        // Assertions
        "androidx.test.ext:junit:${Versions.android.testExt}",
        "androidx.test.ext:truth:${Versions.android.testExt}",
        "com.google.truth:truth:${Versions.jvm.googleTruth}",

        // Espresso dependencies
        "androidx.test.espresso:espresso-core:${Versions.android.testEspresso}",
        "androidx.test.espresso:espresso-contrib:${Versions.android.testEspresso}",
        "androidx.test.espresso:espresso-intents:${Versions.android.testEspresso}",
        "androidx.test.espresso:espresso-accessibility:${Versions.android.testEspresso}",
        "androidx.test.espresso:espresso-web:${Versions.android.testEspresso}",
        "androidx.test.espresso.idling:idling-concurrent:${Versions.android.testEspresso}",

        "androidx.test.espresso:espresso-idling-resource:${Versions.android.testEspresso}")
    }
  }



}