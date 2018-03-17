# KDux: Redux met Kotlin and _got it on_

## Use it in an android project

```groovy
repositories {
    maven {
        url  "https://dl.bintray.com/densebrain/oss" 
    }
}

dependencies {
  implementation "org.densebrain:kdux-core:1+"
  implementation "org.densebrain:kdux-android:1+"
  
  kapt "org.densebrain:kdux-processor:1+"
}
```