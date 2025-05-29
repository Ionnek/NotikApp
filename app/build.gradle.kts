/*
 * Copyright 2025 Ivan Borzykh
 * SPDX-License-Identifier: MIT
 */
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.borzykh.achievera"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.borzykh.achievera"
        minSdk = 30
        targetSdk = 34
        versionCode = 2
        versionName = "1.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures {
            compose = true
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation (libs.app.compat)
    implementation (libs.androidx.core.ktx)
    implementation (platform(libs.compose.bom))
    implementation(libs.androidx.compose.material)
    androidTestImplementation (platform(libs.compose.bom))
    implementation (libs.hilt.compose)
    implementation (libs.material3)

    implementation (libs.paging)
    implementation (libs.paging.compose)

    implementation (libs.ui.tooling.preview)
    debugImplementation (libs.ui.tooling)

    androidTestImplementation (libs.ui.test.junit4)
    debugImplementation (libs.ui.test.manifest)

    implementation (libs.material.icons.core)
    implementation (libs.material.icons.extended)
    implementation (libs.adaptive)

    implementation (libs.activity.compose)
    implementation (libs.lifecycle.viewmodel.compose)
    implementation (libs.runtime.livedata)
    implementation (libs.runtime.rxjava2)
    implementation(libs.coil)

    androidTestImplementation (libs.androidx.espresso.core)

    ksp (libs.room.compiler)

    implementation (libs.room.database)
    implementation (libs.room.kotlin)
    implementation (libs.room.paging)

    implementation (libs.hilt.android)
    ksp (libs.hilt.compiler)

    implementation(libs.gson)
}
