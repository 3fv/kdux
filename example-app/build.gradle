buildscript {
	apply from: "${rootDir}/gradle/versions.gradle"
}

android {
	compileSdkVersion Versions.COMPILE_SDK_VERSION
	
	useLibrary 'android.test.runner'
	useLibrary 'android.test.base'
	useLibrary 'android.test.mock'
	
	defaultConfig {
		applicationId "org.densebrain.kdux.example"
		minSdkVersion Versions.MIN_SDK_VERSION
		targetSdkVersion Versions.TARGET_SDK_VERSION
		versionCode 1
		versionName "1.0"
		testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
		
	}
	buildTypes {
		release {
			minifyEnabled false
			proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
		}
	}
	
	dataBinding {
		enabled = true
	}
	
	testOptions {
		execution 'ANDROIDX_TEST_ORCHESTRATOR'
	}
}

dependencies {
	
	publishProjects.each { dep ->
		implementation project(":${dep}")
	}
	
	implementation(
		"androidx.lifecycle:lifecycle-extensions:${Versions.androidLifecycle}",
		"androidx.lifecycle:lifecycle-common-java8:${Versions.androidLifecycle}",
		"com.android.support:appcompat-v7:${Versions.androidSupport}"
	)
	
	
	androidTestUtil "androidx.test:orchestrator:${Versions.androidTest}"
	
	// Core library
	androidTestImplementation(
		"androidx.test:core:${Versions.androidTestCore}",
		
		// AndroidJUnitRunner and JUnit Rules
		"androidx.test:runner:${Versions.androidTest}",
		"androidx.test:rules:${Versions.androidTest}",
		
		// Assertions
		"androidx.test.ext:junit:${Versions.androidTestExt}",
		"androidx.test.ext:truth:${Versions.androidTestExt}",
		"com.google.truth:truth:${Versions.googleTruth}",
		
		// Espresso dependencies
		"androidx.test.espresso:espresso-core:${Versions.androidTestEspresso}",
		"androidx.test.espresso:espresso-contrib:${Versions.androidTestEspresso}",
		"androidx.test.espresso:espresso-intents:${Versions.androidTestEspresso}",
		"androidx.test.espresso:espresso-accessibility:${Versions.androidTestEspresso}",
		"androidx.test.espresso:espresso-web:${Versions.androidTestEspresso}",
		"androidx.test.espresso.idling:idling-concurrent:${Versions.androidTestEspresso}",
		
		
		"androidx.test.espresso:espresso-idling-resource:${Versions.androidTestEspresso}"
	)
	
}
