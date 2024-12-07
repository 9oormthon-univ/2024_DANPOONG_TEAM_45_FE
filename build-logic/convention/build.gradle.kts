plugins {
    `kotlin-dsl`
}

group = "com.multi.module.buildlogic"

dependencies {
    // Gradle 관련 플러그인
    compileOnly(libs.android.gradlePlugin) // Android Gradle Plugin
    compileOnly(libs.android.tools.common) // Android Common Tools
    compileOnly(libs.kotlin.gradlePlugin) // Kotlin Gradle Plugin
    compileOnly(libs.ksp.gradlePlugin) // Kotlin Symbol Processing (KSP) Plugin
    compileOnly(libs.room.gradlePlugin) // Room Gradle Plugin
    implementation(kotlin("script-runtime"))
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "com.multi.module.android"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidLibrary"){
            id = "com.multi.module.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
    }
}