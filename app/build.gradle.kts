plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.myapplication"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
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
    }
}

dependencies {

    //navigation
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // glide
    implementation(libs.glide)
    annotationProcessor (libs.compiler)

    // viewpager
    implementation(libs.androidx.viewpager2)

    // indicator
    implementation (libs.relex.circleindicator)

    // hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    // kakao
    implementation (libs.com.kakao.sdk.v2.all)

    // datastore
    implementation (libs.androidx.datastore.preferences)

    // okHttp
    implementation(libs.okhttp)
    implementation(platform(libs.okhttp.bom))
    implementation (libs.logging.interceptor)
    implementation (libs.okhttp.urlconnection)

    // retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // gson
    implementation(libs.gson)

    // splash
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // drag and drop
    implementation(libs.androidx.draganddrop)
}

kapt {
    correctErrorTypes = true
}