pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

include(":unityLibrary")
project(":unityLibrary").projectDir = File("C:\\Users\\PC\\Desktop\\MyApplication4\\unityLibrary")

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        flatDir {
            dirs(project(":unityLibrary").projectDir.resolve("libs"))
        }
        maven { url = java.net.URI("https://devrepo.kakao.com/nexus/content/groups/public/") }
    }
}

rootProject.name = "My Application"
include(":app")
include(":core")
include(":data")
include(":feature")
