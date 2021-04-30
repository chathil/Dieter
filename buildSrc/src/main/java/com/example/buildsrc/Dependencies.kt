package com.example.buildsrc

object Versions {
    const val ktlint = "0.40.0"
}

object Libs {
    const val androidGradlePlugin = "com.android.tools.build:gradle:7.0.0-alpha14"
    const val junit = "junit:junit:4.13.2"
    const val material = "com.google.android.material:material:1.3.0"
    const val kotlinxSerialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.1.0"
    const val kotlinxCollections = "org.jetbrains.kotlinx:kotlinx-collections-immutable-jvm:0.3.4"
    const val hiltNavigationCompose = "androidx.hilt:hilt-navigation-compose:1.0.0-alpha01"
    const val coilGif = "io.coil-kt:coil-gif:1.2.0"

    object Hilt {
        private const val version = "2.33-beta"
        const val hiltCompiler = "com.google.dagger:hilt-compiler:$version"
        const val hiltAndroid = "com.google.dagger:hilt-android:$version"
        const val hiltGradlePlugin = "com.google.dagger:hilt-android-gradle-plugin:$version"

    }

    object Kotlin {
        private const val version = "1.4.32"
        const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$version"
        const val extensions = "org.jetbrains.kotlin:kotlin-android-extensions:$version"
    }

    object Retrofit {
        const val retrofit = "com.squareup.retrofit2:retrofit:2.9.0"
        const val coroutinesAdapter =
            "com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2"
        const val kotlinxSerializationConverter =
            "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0"
    }

    object Coroutines {
        private const val version = "1.4.3"
        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
        const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
        const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$version"
    }

    object Accompanist {
        const val version = "0.8.1"
        const val glide = "com.google.accompanist:accompanist-glide:$version"
        const val insets = "com.google.accompanist:accompanist-insets:$version"
        const val pager = "com.google.accompanist:accompanist-pager:$version"
        const val pagerIndicators = "com.google.accompanist:accompanist-pager-indicators:$version"
    }

    object Firebase {
        private const val version = "27.0.0"
        const val bom = "com.google.firebase:firebase-bom:27.0.0"
        const val analyticsKtx = "com.google.firebase:firebase-analytics-ktx"
        const val databaseKtx = "com.google.firebase:firebase-database-ktx"
    }

    object AndroidX {
        const val appcompat = "androidx.appcompat:appcompat:1.2.0"
        const val coreKtx = "androidx.core:core-ktx:1.3.2"
        const val navigation = "androidx.navigation:navigation-compose:1.0.0-alpha10"

        object Activity {
            const val activityCompose = "androidx.activity:activity-compose:1.3.0-alpha06"
        }

        object Compose {
            const val snapshot = ""
            const val version = "1.0.0-beta04"

            const val foundation = "androidx.compose.foundation:foundation:$version"
            const val layout = "androidx.compose.foundation:foundation-layout:$version"
            const val material = "androidx.compose.material:material:$version"
            const val materialIconsExtended =
                "androidx.compose.material:material-icons-extended:$version"
            const val runtime = "androidx.compose.runtime:runtime:$version"
            const val tooling = "androidx.compose.ui:ui-tooling:$version"
            const val uiUtil = "androidx.compose.ui:ui-util:${version}"
            const val test = "androidx.compose.ui:ui-test:$version"
            const val uiTest = "androidx.compose.ui:ui-test-junit4:$version"


        }

        object Room {
            private const val version = "2.2.6"
            const val runtime = "androidx.room:room-runtime:$version"
            const val ktx = "androidx.room:room-ktx:$version"
            const val legacySupport = "androidx.legacy:legacy-support-v4:1.0.0"
            const val compiler = "androidx.room:room-compiler:$version"
        }

        object Test {
            private const val version = "1.3.0"
            const val core = "androidx.test:core:$version"
            const val rules = "androidx.test:rules:$version"

            object Ext {
                private const val version = "1.1.2"
                const val junit = "androidx.test.ext:junit-ktx:$version"

            }

            const val espressoCore = "androidx.test.espresso:espresso-core:3.3.0"
        }

        object Lifecycle {
            private const val version = "2.3.0"
            const val extensions = "androidx.lifecycle:lifecycle-extensions:$version"

            //            const val viewmodel = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
            const val runtimeKtx = "androidx.lifecycle:lifecycle-runtime-ktx:2.3.1"
            const val viewModelCompose =
                "androidx.lifecycle:lifecycle-viewmodel-compose:1.0.0-alpha02"
        }

    }
}

object Urls {
    const val mavenCentralSnapshotRepo = "https://oss.sonatype.org/content/repositories/snapshots/"
    const val composeSnapshotRepo = "https://androidx.dev/snapshots/builds/" +
            "${Libs.AndroidX.Compose.snapshot}/artifacts/repository/"
}