import com.example.buildsrc.Libs

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("com.google.gms.google-services")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = 30
    buildToolsVersion = "30.0.3"

    defaultConfig {
        applicationId = "com.example.dieter"
        minSdk = 21
        targetSdk = 30
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
        useIR = true
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Libs.AndroidX.Compose.version
    }
    packagingOptions {
        resources.excludes.add("META-INF/licenses/**")
        resources.excludes.add("META-INF/AL2.0")
        resources.excludes.add("META-INF/LGPL2.1")
    }
}

dependencies {
    implementation(Libs.Kotlin.stdlib)
    implementation(Libs.material)
    implementation(Libs.kotlinxSerialization)
    implementation(Libs.kotlinxCollections)
    implementation(Libs.hiltNavigationCompose)

    implementation(Libs.Coroutines.core)
    implementation(Libs.Coroutines.android)
    implementation(Libs.Coroutines.test)

    implementation(Libs.AndroidX.coreKtx)
    implementation(Libs.AndroidX.appcompat)
    implementation(Libs.AndroidX.navigation)
    implementation(Libs.AndroidX.Activity.activityCompose)
    implementation(Libs.AndroidX.Lifecycle.viewModelCompose)
    implementation(Libs.AndroidX.Lifecycle.runtimeKtx)

    implementation(Libs.AndroidX.Compose.material)
    implementation(Libs.AndroidX.Compose.tooling)
    implementation(Libs.AndroidX.Compose.layout)
    implementation(Libs.AndroidX.Compose.materialIconsExtended)
    implementation(Libs.AndroidX.Compose.uiUtil)
    implementation(Libs.AndroidX.Compose.runtime)
    implementation(Libs.AndroidX.Compose.foundation)

    implementation(Libs.Accompanist.insets)
    implementation(Libs.Accompanist.glide)
    implementation(Libs.Accompanist.pager)
    implementation(Libs.Accompanist.pagerIndicators)

    implementation(platform(Libs.Firebase.bom))
    implementation(Libs.Firebase.analyticsKtx)
    implementation(Libs.Firebase.databaseKtx)

    kapt(Libs.Hilt.hiltCompiler)
    implementation(Libs.Hilt.hiltAndroid)


    implementation(Libs.AndroidX.Room.runtime)
    implementation(Libs.AndroidX.Room.ktx)
    implementation(Libs.AndroidX.Room.legacySupport)
    kapt(Libs.AndroidX.Room.compiler)

    implementation(Libs.Retrofit.retrofit)
    implementation(Libs.Retrofit.coroutinesAdapter)
    implementation(Libs.Retrofit.kotlinxSerializationConverter)

    androidTestImplementation(Libs.junit)
    androidTestImplementation(Libs.AndroidX.Test.Ext.junit)
    androidTestImplementation(Libs.AndroidX.Test.core)
    androidTestImplementation(Libs.AndroidX.Test.rules)
    androidTestImplementation(Libs.AndroidX.Test.espressoCore)
    androidTestImplementation(Libs.AndroidX.Compose.uiTest)

    // androidx.test is forcing JUnit, 4.12. This forces it to use 4.13.2
    configurations.configureEach {
        resolutionStrategy {
            force(Libs.junit)
        }
    }

}