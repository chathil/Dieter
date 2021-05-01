// Top-level build file where you can add configuration options common to all sub-projects/modules.
import com.example.buildsrc.Libs

buildscript {
    repositories {
        google()
        mavenCentral()
        maven {
            setUrl("https://kotlin.bintray.com/kotlinx")
        }
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.0.0-alpha15")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.32")
        classpath("com.google.gms:google-services:4.3.5")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.33-beta")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle.kts files
    }
}


plugins {
    id("com.diffplug.spotless").version("5.7.0")
}

subprojects {
    apply {
        plugin("com.diffplug.spotless")
    }
    spotless {
        kotlin {
            target("**/*.kt")
            targetExclude("$buildDir/**/*.kt")
            targetExclude("bin/**/*.kt")
            ktlint("0.40.0")
//            licenseHeaderFile rootProject.file('spotless/copyright.kt') /* add copyright header */
        }
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        // Treat all Kotlin warnings as errors
        allWarningsAsErrors = true

        freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn"

        // Enable experimental coroutines APIs, including Flow
        freeCompilerArgs =
            freeCompilerArgs + "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
        freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlinx.coroutines.FlowPreview"
        freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.Experimental"

        // Set JVM target to 1.8
        jvmTarget = "1.8"
    }
}