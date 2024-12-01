plugins {
    alias(libs.plugins.multi.module.android.application)
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.myapplication"

    buildFeatures {
        dataBinding = true
    }
}

dependencies {

    //navigation
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // glide
    implementation(libs.glide)
    implementation(libs.androidx.paging.common.android)
    ksp (libs.compiler)

    // viewpager
    implementation(libs.androidx.viewpager2)

    // indicator
    implementation (libs.relex.circleindicator)

    // hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

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

    // coroutines
    implementation(libs.kotlinx.coroutines.android)

    //lottie
    implementation (libs.lottie)

    //room
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
}
