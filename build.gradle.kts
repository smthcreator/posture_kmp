plugins {
    // Kotlin 2.0.20 (совместим с Xcode 16 / iOS 18 SDK)
    kotlin("multiplatform") version "2.0.21" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.21" apply false

    // Compose Multiplatform 1.7.1
    id("org.jetbrains.compose") version "1.7.1" apply false

    // Android Gradle Plugin 8.5.2 (совместим с Gradle 8.7)
    id("com.android.application") version "8.5.2" apply false
    id("com.android.library") version "8.5.2" apply false
}