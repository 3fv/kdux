plugins {
  base
  java
  kotlin("jvm") version "1.3.21"
}

repositories {
  google()
  jcenter()
  mavenCentral()
  maven {
    setUrl("https://dl.bintray.com/kotlin/kotlin-eap")
  }
}

dependencies {
  compile(kotlin("stdlib"))
  compile(gradleApi())
  compile("com.android.tools.build:gradle:3.3.2")
  compile("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.21")
  compile("org.jetbrains.kotlin:kotlin-frontend-plugin:0.0.45")
  //runtime("org.jetbrains.kotlin:kotlin-frontend-plugin:0.0.45")
}