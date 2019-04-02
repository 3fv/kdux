
object Versions {
  val kotlin = "1.3.21"
  val kotlinCoroutine = "1.1.1"

  val bintray                = "1.8.4"
  val kotlinFrontend = "0.0.45"


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
  }

  object android {
    val minSdk = 24
    val targetSdk = 28
    val compileSdk = 28

    val anko                   = "0.10.5"
    val gradle          = "3.3.2"
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
  object kotlin {
    object coroutines {
      val common = "org.jetbrains.kotlinx:kotlinx-coroutines-core-common:${Versions.kotlinCoroutine}"
      val jvm = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinCoroutine}"
      val js = "org.jetbrains.kotlinx:kotlinx-coroutines-core-js:${Versions.kotlinCoroutine}"
    }
  }


  object jvm {
    val slf4j = "org.slf4j:slf4j-api:${Versions.jvm.slf4j}"
  }

}