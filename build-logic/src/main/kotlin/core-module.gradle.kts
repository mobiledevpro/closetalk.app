plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("kotlin-convention")
}

val projectName = project.name.replace("_", ".")

android {
    namespace = "com.mobiledevpro.$projectName"
    compileSdk = libs.versionInt("sdk.compile")

    defaultConfig {
        minSdk = libs.versionInt("sdk.min")
    }

    compileOptions {
        android.compileOptions.isCoreLibraryDesugaringEnabled = true

        targetCompatibility = JavaVersion.VERSION_17
        sourceCompatibility = JavaVersion.VERSION_17
    }

    kotlin {
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }
    }

    flavorDimensions += listOf("default")

    productFlavors {
        create("production") {
            dimension = "default"
        }

        create("dev") {
            dimension = "default"
        }
    }

    println("# Namespace: $projectName")
    println("# Compile SDK version: ${libs.versionStr("sdk.compile")}")
    println("# Min SDK version: ${libs.versionStr("sdk.min")}")
    println("# Compose Compiler version: ${libs.versionStr("compose.compiler")}")
}

android.sourceSets {
    getByName("main") {
        kotlin.srcDir("src/main/kotlin")
        res.srcDir("src/main/res")
    }

    getByName("production") {
        kotlin.srcDir("src/production/kotlin")
        res.srcDir("src/production/res")
    }
    getByName("dev") {
        kotlin.srcDir("src/dev/kotlin")
        res.srcDir("src/dev/res")
    }
}

dependencies {
    coreLibraryDesugaring(libs.library("desugaring"))
}