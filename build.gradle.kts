// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    id("com.google.dagger.hilt.android") version "2.52" apply false
    id("com.google.devtools.ksp") version "1.9.24-1.0.20"
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.android.library) apply false
}

buildscript {
    dependencies {
        classpath(libs.androidx.navigation.safe.args.gradle.plugin)
    }
}