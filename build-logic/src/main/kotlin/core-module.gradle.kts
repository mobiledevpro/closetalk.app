plugins {
    id("com.android.library")
    id("kotlin-convention")
}

val projectName = project.name.replace("_", ".")

android {
    namespace = "com.mobiledevpro.$projectName"
    compileSdk {
        version = release(libs.versionInt("sdk.compile")) {
            minorApiLevel = libs.versionInt("sdk.compile.minor")
        }
    }

    defaultConfig {
        minSdk = libs.versionInt("sdk.min")
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
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
    println("# Compile SDK version: ${libs.versionStr("sdk.compile")}.${libs.versionStr("sdk.compile.minor")}")
    println("# Min SDK version: ${libs.versionStr("sdk.min")}")
    println("# Compose Compiler version: ${libs.versionStr("compose.compiler")}")
}

kotlin {
    jvmToolchain(libs.versionInt("jdkVersion"))
}

android.sourceSets {
    getByName("main") {
        kotlin.directories += "src/main/kotlin"
        res.directories += "src/main/res"
    }

    getByName("production") {
        kotlin.directories += "src/production/kotlin"
        res.directories += "src/production/res"
    }
}

dependencies {
    coreLibraryDesugaring(libs.library("desugaring"))
}