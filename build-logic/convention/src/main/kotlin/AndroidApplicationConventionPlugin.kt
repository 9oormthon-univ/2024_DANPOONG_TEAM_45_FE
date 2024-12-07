import com.android.build.api.dsl.ApplicationExtension
import convention.ExtensionType
import convention.configureBuildTypes
import convention.configureCommonDependencies
import convention.configureDependencies
import convention.configureKotlinAndroid
import convention.configureLibraryDependencies
import convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

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

                signingConfigs{
                    create("release"){
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
                        signingConfig = signingConfigs.getByName("release")  // 릴리즈 서명 구성 연결
                    }
                }

                configureKotlinAndroid(this)

                configureCommonDependencies()

                configureDependencies()

                configureLibraryDependencies()

                configureBuildTypes(
                    commonExtension = this,
                    extensionType = ExtensionType.APPLICATION
                )
            }
        }
    }

}



