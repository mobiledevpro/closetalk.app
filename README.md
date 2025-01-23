# Android Chat App entirely built with Jetpack Compose (in development)

![Run Unit Tests](https://github.com/mobiledevpro/closetalk.app/actions/workflows/tests.yml/badge.svg)
[![CodeFactor](https://www.codefactor.io/repository/github/mobiledevpro/closetalk.app/badge)](https://www.codefactor.io/repository/github/mobiledevpro/closetalk.app)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=mobiledevpro_Jetpack-Compose-ChatApp-Template&metric=alert_status)](https://sonarcloud.io/dashboard?id=mobiledevpro_Jetpack-Compose-ChatApp-Template)

[![Kotlin Version](https://img.shields.io/badge/Kotlin-2.0.20-blue.svg?style=flat-square)](http://kotlinlang.org/)
[![Compose Bom](https://img.shields.io/badge/Compose%20Bom-2024.11.00-blue.svg?style=flat-square)]([http://kotlinlang.org/](https://developer.android.com/jetpack/compose/bom/bom-mapping))
[![Gradle](https://img.shields.io/badge/Gradle-8.7.3-blue.svg?style=flat-square)](https://developer.android.com/build/releases/gradle-plugin)
[![API](https://img.shields.io/badge/Min%20SDK-29%20[Android%2010]-blue.svg?style=flat-square)](https://github.com/AndroidSDKSources/android-sdk-sources-list)
[![Target SDK](https://img.shields.io/badge/Target%20SDK-35%20[Android%2015]-blue.svg?style=flat-square)](https://developer.android.com/about/versions/13)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg?style=flat-square)](http://www.apache.org/licenses/LICENSE-2.0)
[![Android Studio](https://img.shields.io/badge/Android%20Studio%20Ladybug-2024.2.2-orange.svg?style=flat-square)](https://developer.android.com/studio/preview)

![GitHub last commit](https://img.shields.io/github/last-commit/mobiledevpro/closetalk.app?color=red&style=flat-square)
##
### Try demo:
[<img src="https://github.com/mobiledevpro/closetalk.app/assets/5750211/56e09ffa-faa5-4ad1-8ad8-4ee35957870b" width="250" alt="Demo"/>](https://play.google.com/store/apps/details?id=com.mobiledevpro.closetalk.app&utm_source=landing)

##
![closetalk_github_social_preview](https://github.com/mobiledevpro/closetalk.app/assets/5750211/343f1ab5-54e4-41c2-a554-af0526aee382)

[![Youtube](https://img.shields.io/badge/-youtube-red?logo=youtube&message=Youtube&style=for-the-badge&label=Watch+on)](https://www.youtube.com/playlist?list=PL9IBbMupfHWrW419OtGlzc7cBEMNqyLa4)

##
### Apps built from this template: 
- [Screenshot Maker - NeonFrame](https://NeonFrame.app) - Make eye-catching screenshots you share on Twitter, Instagram, and others.
 
##
### Tech. stack

* [Kotlin](https://kotlinlang.org/docs/getting-started.html)
* [Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)
* [Jetpack Compose](https://developer.android.com/jetpack/compose) modern toolkit for building native UI
* [Jetpack libs: Navigation, Compose, etc.](https://developer.android.com/jetpack)
* [Material 3](https://m3.material.io/)
* [Koin](https://insert-koin.io/docs/reference/koin-android/compose) for dependency injection
* [Coil](https://coil-kt.github.io/coil/compose/) for image loading
* MVI + modularization with a clean architecture
* GitHub Actions [Complete Guide](https://www.patreon.com/mobiledevpro/shop/power-of-github-actions-complete-guide-943321)

## Notes

Plugin to generate Compose Compiler metrics (root/build.gradle.kts)
[Interpreting Compose Compiler Metrics](https://github.com/JetBrains/kotlin/blob/master/plugins/compose/design/compiler-metrics.md)

```kotlin
subprojects {
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().all {
        kotlinOptions.freeCompilerArgs += listOf(
            "-P",
            "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=" +
                    project.buildDir.absolutePath + "/compose_metrics"
        )
        kotlinOptions.freeCompilerArgs += listOf(
            "-P",
            "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=" +
                    project.buildDir.absolutePath + "/compose_metrics"
        )
    }
}
```

##
## UI testing with [Maestro](https://maestro.mobile.dev/):

* Install Maestro: run in terminal ```curl -Ls "https://get.maestro.mobile.dev" | bash```
* Install the app on emulator (doesn't work with physical device)
* Run the flow: run in terminal ```maestro test -c maestro/people-profile-flow.yaml```
* [Sample config](maestro/people-profile-flow.yaml)

##
## Module Graph

```mermaid
%%{
  init: {
    'theme': 'base',
    'themeVariables': {"primaryTextColor":"#fff","primaryColor":"#5a4f7c","primaryBorderColor":"#5a4f7c","lineColor":"#f5a623","tertiaryColor":"#40375c","fontSize":"12px"}
  }
}%%

graph LR
  subgraph :core
    :core:navigation["navigation"]
    :core:ui["ui"]
    :core:di["di"]
    :core:domain["domain"]
    :core:coroutines["coroutines"]
    :core:util["util"]
    :core:analytics["analytics"]
  end
  subgraph :feature
    :feature:home["home"]
    :feature:onboarding["onboarding"]
    :feature:subscription["subscription"]
    :feature:chat_list["chat_list"]
    :feature:people["people"]
    :feature:user_profile["user_profile"]
  end
  :core:navigation --> :core:ui
  :core:navigation --> :core:di
  :core:navigation --> :core:domain
  :core:navigation --> :feature:home
  :core:navigation --> :feature:onboarding
  :core:navigation --> :feature:subscription
  :core:navigation --> :feature:chat_list
  :core:navigation --> :feature:people
  :core:navigation --> :feature:user_profile
  :app --> :core:navigation
  :feature:home --> :core:ui
  :feature:home --> :core:di
  :feature:home --> :core:domain
  :feature:home --> :core:coroutines
  :feature:home --> :core:util
  :feature:home --> :core:analytics

classDef focus fill:#FA8140,stroke:#fff,stroke-width:2px,color:#fff;
class :core:navigation focus
class :feature:home focus
```
### How to create the module graph

```kotlin
 ./gradlew createModuleGraph
```

##
## Author:

<a href="https://github.com/dmitriy-chernysh" target="_blank">
  <img src="https://s.gravatar.com/avatar/72c649d298a8f0f088fd0850e19b9147?s=400" width="70" align="left">
</a>

**Dmitri Chernysh**

[![Youtube](https://img.shields.io/badge/-youtube-red?logo=youtube&message=Youtube&style=for-the-badge)](https://www.youtube.com/@mobiledevpro?sub_confirmation=1)
[![Instagram](https://img.shields.io/badge/-instagram-E4405F?logo=instagram&message=Behind+the+scenes+in+Storiesn&style=for-the-badge&logoColor=white)](https://www.instagram.com/mobiledevpro/)
[![Twitter](https://img.shields.io/badge/-twitter-1DA1F2?logo=twitter&style=for-the-badge&logoColor=white)](https://twitter.com/mobiledev_pro)
[![Linkedin](https://img.shields.io/badge/-linkedin-0A66C2?logo=linkedin&style=for-the-badge&logoColor=white)](https://www.linkedin.com/in/dmitriychernysh/)

## License:

Copyright 2023 Dmitri Chernysh

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

## Thanks for the support
**Stargazers**

[![Stargazers repo roster for @mobiledevpro/closetalk.app](http://reporoster.com/stars/dark/mobiledevpro/closetalk.app)](https://github.com/mobiledevpro/closetalk.app/stargazers)

**Forkers**

[![Forkers repo roster for @mobiledevpro/closetalk.app](http://reporoster.com/forks/dark/mobiledevpro/closetalk.app)](https://github.com/mobiledevpro/closetalk.app/network/members)