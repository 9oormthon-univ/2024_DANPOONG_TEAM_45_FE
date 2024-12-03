import com.android.build.api.dsl.ApplicationExtension
import convention.ExtensionType
import convention.configureBuildTypes
import convention.configureKotlinAndroid
import convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
                plugins.apply("com.google.devtools.ksp")
                plugins.apply("com.google.dagger.hilt.android")
                plugins.apply("androidx.navigation.safeargs.kotlin")
            }

            extensions.configure<ApplicationExtension> {
                defaultConfig {
                    applicationId = libs.findVersion("projectApplicationId").get().toString()
                    targetSdk = libs.findVersion("projectTargetSdkVersion").get().toString().toInt()
                    minSdk = libs.findVersion("projectMinSdkVersion").get().toString().toInt()
                    versionCode = libs.findVersion("projectVersionCode").get().toString().toInt()
                    versionName = libs.findVersion("projectVersionName").get().toString()
                    testInstrumentationRunner = libs.findVersion("testInstrumentationRunner").get().toString()
                }

                configureKotlinAndroid(this)
                configureDependencies()
                configureBuildTypes(
                    commonExtension = this,
                    extensionType = ExtensionType.APPLICATION
                )
            }
        }
    }
}
