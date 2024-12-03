import com.android.build.api.dsl.LibraryExtension
import convention.ExtensionType
import convention.configureBuildTypes
import convention.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
                plugins.apply("com.google.devtools.ksp")
                plugins.apply("com.google.dagger.hilt.android")
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                configureDependencies()
                configureBuildTypes(
                    commonExtension = this,
                    extensionType = ExtensionType.LIBRARY
                )

                defaultConfig {
                    testInstrumentationRunner = "androidx.text.runner.AndroidJUnitRunner"
                    consumerProguardFiles("consumer-rules.pro")
                }
            }
        }
    }
}

fun Project.configureDependencies() {
    dependencies {
        // Hilt Dependencies (KSP)
        add("implementation", "com.google.dagger:hilt-android:2.52")
        add("ksp", "com.google.dagger:hilt-android-compiler:2.52")

        // Room Dependencies (KSP)
        add("implementation", "androidx.room:room-runtime:2.6.1")
        add("ksp", "androidx.room:room-compiler:2.6.1")
        add("implementation", "androidx.room:room-ktx:2.6.1")

        //data store
        add("implementation", "androidx.datastore:datastore-preferences:1.1.1")

        // Retrofit
        add("implementation", "com.squareup.retrofit2:retrofit:2.11.0")
        add("implementation", "com.squareup.retrofit2:converter-gson:2.11.0")

        // OkHttp
        add("implementation", "com.squareup.okhttp3:okhttp:4.12.0")
        add("implementation", "com.squareup.okhttp3:logging-interceptor:4.12.0")
        add("implementation", "com.squareup.okhttp3:okhttp-urlconnection:4.12.0")
        add("implementation", platform("com.squareup.okhttp3:okhttp-bom:4.12.0")) // BOM 사용

        // Gson
        add("implementation", "com.google.code.gson:gson:2.11.0")
    }
}
