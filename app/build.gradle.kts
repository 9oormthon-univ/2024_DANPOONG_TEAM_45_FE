plugins {
    alias(libs.plugins.multi.module.android.application)
}

android {
    namespace = "com.example.myapplication"
    ndkVersion = "25.2.9519653" // NDK 명시
    buildFeatures {
        dataBinding = true
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true  // 난독화 활성화
            isShrinkResources = true  // 리소스 축소
            signingConfig = signingConfigs.getByName("release")  // 릴리즈 서명 구성 연결
        }
    }
}

dependencies{
    implementation(project(":unityLibrary"))
}
