plugins {
    kotlin("multiplatform")
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.native.cocoapods")
    id("com.android.library")
}

kotlin {
    androidTarget()

    iosX64()
    iosArm64()
    iosSimulatorArm64()
        
    cocoapods {
        // Данные для Podspec
        summary = "Shared code for PostureKMP"
        homepage = "https://example.com/posturekmp"
        ios.deploymentTarget = "15.5"
        podfile = project.file("../PostureKMPiOS/Podfile")
        version = "0.1.0"

        // Фреймворк для Xcode/Pods
        framework {
            baseName = "shared"
            isStatic = true
        }
        
        // pod("MediaPipeTasksVision") // перенесли в iosApp (Swift), чтобы избежать cinterop
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)
                implementation(compose.ui)
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("androidx.activity:activity-compose:1.9.2")
                implementation("androidx.camera:camera-core:1.3.4")
                implementation("androidx.camera:camera-camera2:1.3.4")
                implementation("androidx.camera:camera-lifecycle:1.3.4")
                implementation("androidx.camera:camera-view:1.3.4")
                implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.4")
                implementation("com.google.mediapipe:tasks-vision:0.10.14")
            }
        }
        val iosMain by creating {
            dependsOn(commonMain)
        }
        val iosX64Main by getting { dependsOn(iosMain) }
        val iosArm64Main by getting { dependsOn(iosMain) }
        val iosSimulatorArm64Main by getting { dependsOn(iosMain) }
    }
        // Opt-in для некоторых экспериментальных API Compose MPP
    sourceSets.all {
        languageSettings.optIn("org.jetbrains.compose.ExperimentalComposeLibrary")
    }
}

android {
    namespace = "ai.posture.shared"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}