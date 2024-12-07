plugins {
    alias(libs.plugins.multi.module.android.application)
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}
dependencies{
    implementation(project(":data"))
}

android {
    namespace = "com.example.myapplication"

}
