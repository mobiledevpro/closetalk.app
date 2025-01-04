pluginManagement {
    includeBuild("build-logic")
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "App-Template-Compose"

include(":app")
include(":core:ui")
include(":core:navigation")
include(":core:domain")
include(":core:coroutines")

include(":feature:home")
include(":feature:onboarding")
include(":feature:subscription")
include(":feature:chat_list")
include(":feature:people_list")
include(":feature:user_profile")
include(":feature:people_profile")
include(":feature:people")
include(":core:di")
include(":core:util")
include(":core:analytics")
include(":feature:settings_core")
include(":feature:main")
