package convention

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal fun Project.configureKotlinAndroid(
    commonExtension: CommonExtension<*, *, *, *, *, *>
) {
    commonExtension.apply {
        compileSdk = libs.findVersion("projectCompileSdkVersion").get().toString().toInt()

        defaultConfig {
            minSdk = libs.findVersion("projectMinSdkVersion").get().toString().toInt()
        }

        compileOptions {
            isCoreLibraryDesugaringEnabled = true
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }
    }

    configureKotlin()

    dependencies {
        "coreLibraryDesugaring"(libs.findLibrary("desugar-jdk-libs").get())
    }
}

private fun Project.configureKotlin() {
    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
}

fun Project.configureDependencies() {

    val imp = "implementation"
    val ksp = "ksp"
    dependencies {
        // Hilt Dependencies (KSP)
        add(imp, "com.google.dagger:hilt-android:2.52")
        add(ksp, "com.google.dagger:hilt-android-compiler:2.52")

        // Room Dependencies (KSP)
        add(imp, "androidx.room:room-runtime:2.6.1")
        add(ksp, "androidx.room:room-compiler:2.6.1")
        add(imp, "androidx.room:room-ktx:2.6.1")

        //data store
        add(imp, "androidx.datastore:datastore-preferences:1.1.1")

        // Retrofit
        add(imp, "com.squareup.retrofit2:retrofit:2.11.0")
        add(imp, "com.squareup.retrofit2:converter-gson:2.11.0")

        // OkHttp
        add(imp, "com.squareup.okhttp3:okhttp:4.12.0")
        add(imp, "com.squareup.okhttp3:logging-interceptor:4.12.0")
        add(imp, "com.squareup.okhttp3:okhttp-urlconnection:4.12.0")
        add(imp, platform("com.squareup.okhttp3:okhttp-bom:4.12.0")) // BOM 사용

        // Gson
        add(imp, "com.google.code.gson:gson:2.11.0")
    }
}

fun Project.configureCommonDependencies() {
    val imp = "implementation"
    dependencies {
        add("implementation", libs.findLibrary("androidx.core.ktx").get())
        add("implementation", libs.findLibrary("androidx.appcompat").get())
        add("implementation", libs.findLibrary("material").get())
        add("implementation", libs.findLibrary("junit").get())
        add("androidTestImplementation", libs.findLibrary("androidx.junit").get())
        add("androidTestImplementation", libs.findLibrary("androidx.espresso.core").get())
    }
}

fun Project.configureLibraryDependencies() {
    dependencies {
        // Navigation
        add("implementation", libs.findLibrary("androidx.navigation.fragment.ktx").get())
        add("implementation", libs.findLibrary("androidx.navigation.ui.ktx").get())

        // Glide
        add("implementation", libs.findLibrary("glide").get())
        add("implementation", libs.findLibrary("androidx.paging.common.android").get())
        add("ksp", libs.findLibrary("compiler").get())

        // ViewPager2
        add("implementation", libs.findLibrary("androidx.viewpager2").get())

        // Indicator
        add("implementation", libs.findLibrary("relex.circleindicator").get())

        // Kakao SDK
        add("implementation", libs.findLibrary("com.kakao.sdk.v2.all").get())

        // Splash Screen
        add("implementation", libs.findLibrary("androidx.core.splashscreen").get())

        // Activity & ConstraintLayout
        add("implementation", libs.findLibrary("androidx.activity").get())
        add("implementation", libs.findLibrary("androidx.constraintlayout").get())

        // Drag and Drop
        add("implementation", libs.findLibrary("androidx.draganddrop").get())

        // Coroutines
        add("implementation", libs.findLibrary("kotlinx.coroutines.android").get())

        // Lottie
        add("implementation", libs.findLibrary("lottie").get())

        // Profile Installer
        add("implementation", libs.findLibrary("androidx.profileinstaller").get())
    }
}


val Project.libs: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

