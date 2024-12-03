plugins {
    alias(libs.plugins.multi.module.android.application)
}

android {
    namespace = "com.example.myapplication"

    buildFeatures {
        dataBinding = true
    }
    signingConfigs {
        create("release") {
            storeFile = file("/keystore/moomoo.jks")  // 키스토어 파일 경로
            storePassword = "990329"  // 키스토어 비밀번호
            keyAlias = "moomoo"  // 키 별칭
            keyPassword = "990329"  // 키 비밀번호
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true  // 난독화 활성화
            isShrinkResources = true  // 리소스 축소
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")  // 릴리즈 서명 구성 연결
        }
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


    implementation (libs.androidx.profileinstaller)

}
