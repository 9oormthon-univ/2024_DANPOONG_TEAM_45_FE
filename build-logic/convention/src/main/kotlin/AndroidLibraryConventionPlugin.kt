import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import com.multi.module.convention.ExtensionType
import com.multi.module.convention.configureBuildTypes
import com.multi.module.convention.configureKotlinAndroid
import com.multi.module.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<LibraryExtension>{
                configureKotlinAndroid(this)

                configureBuildTypes(
                    commonExtension = this,
                    extensionType = ExtensionType.LIBRARY
                )

                defaultConfig{
                    testInstrumentationRunner = "androidx.text.runner.AndroidJUnitRunner"
                    consumerProguardFiles("consumer-rules.pro")
                }
            }
        }
    }
}
