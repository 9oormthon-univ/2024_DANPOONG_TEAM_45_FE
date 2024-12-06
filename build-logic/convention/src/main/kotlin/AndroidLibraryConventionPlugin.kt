import com.android.build.api.dsl.LibraryExtension
import convention.ExtensionType
import convention.configureBuildTypes
import convention.configureCommonDependencies
import convention.configureDependencies
import convention.configureKotlinAndroid
import convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

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

                configureCommonDependencies()

                configureDependencies()

                configureBuildTypes(
                    commonExtension = this,
                    extensionType = ExtensionType.LIBRARY
                )

                defaultConfig {
                    testInstrumentationRunner = libs.findVersion("testInstrumentationRunner").get().toString()
                }
            }
        }
    }
}


