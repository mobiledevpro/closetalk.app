import com.android.build.gradle.AppExtension
import java.io.FileNotFoundException
import java.util.Properties

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    alias(libs.plugins.compose.compiler)

    alias(libs.plugins.google.services)
    alias(libs.plugins.crashlytics)
    alias(libs.plugins.performance.monitor)
}

android {
    namespace = "com.mobiledevpro.apptemplate.compose"
    compileSdk = libs.versions.sdk.compile.get().toInt()


    defaultConfig {
        applicationId = "com.mobiledevpro"
        minSdk = libs.versions.sdk.min.get().toInt()
        targetSdk = libs.versions.sdk.target.get().toInt()
        versionCode = libs.versions.app.version.code.get().toInt()
        versionName = libs.versions.app.version.name.get()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        val keystoreProperties = Properties()
        try {
            keystoreProperties.load(File(rootDir, "keystore.properties").inputStream())

            create("release") {
                storeFile = file("release.jks")
                storePassword = keystoreProperties["KSTOREPWD"] as String
                keyAlias = keystoreProperties["KEYSTORE_ALIAS"] as String
                keyPassword = keystoreProperties["KEYPWD"] as String
            }
        } catch (e: FileNotFoundException) {
            println("Warning: keystore.properties file not found.")
        }
    }

    buildTypes {
        getByName("debug") {
            isDebuggable = true

            firebaseCrashlytics {
                mappingFileUploadEnabled = false
            }
        }

        getByName("release") {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            signingConfig = try {
                signingConfigs.getByName("release")
            } catch (e: UnknownDomainObjectException) {
                println("SigningConfig with not found. Skipping...")
                null
            }

            firebaseCrashlytics {
                mappingFileUploadEnabled = true
            }
        }
    }

    flavorDimensions += listOf("default")

    productFlavors {
        create("production") {
            dimension = "default"
            applicationIdSuffix = ".closetalk.app"
        }

        create("dev") {
            dimension = "default"
            applicationIdSuffix = ".apptemplate.compose"
            isDefault = true
        }
    }

    compileOptions {
        android.compileOptions.isCoreLibraryDesugaringEnabled = true

        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin {
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }
    }


    buildFeatures {
        compose = true
        buildConfig = true
    }

    packaging {
        resources.excludes.add("/META-INF/{AL2.0,LGPL2.1}")
    }

    // There are duplicated classes in firebase lib and in com.google.protobuf
    configurations {
        implementation {
            exclude(group = "com.google.firebase", module = "protolite-well-known-types")
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    coreLibraryDesugaring(libs.desugaring)
    implementation(libs.bundles.lifecycle)

    implementation(projects.core.navigation)

    implementation(libs.firebase.performance)
    implementation(projects.core.analytics)

    testApi(libs.bundles.test.common)

    // Exclude Firebase from your Android tests due to errors "Could not resolve all files for configuration"
    androidTestImplementation(libs.firebase.performance) {
        exclude(group = "com.google.firebase", module = "firebase-perf")
    }
    androidTestImplementation(libs.firebase.analytics) {
        exclude(group = "com.google.firebase", module = "firebase-analytics")
    }
    androidTestImplementation(libs.firebase.crashlytics) {
        exclude(group = "com.google.firebase", module = "firebase-crashlytics")
    }

    implementation(projects.feature.main)
}


//This task is used for .AAB file renaming to following format:
//{application_id}-v{version_name}-build{version_code}-{product_flavor}-{build_type}.aab

tasks.register("renameBundle") {
    doLast {
        val androidExtension = project.extensions.findByType(AppExtension::class.java)

        androidExtension?.also { android ->

            println("Variant variants: ${android.applicationVariants}")

            android.applicationVariants.forEach { variant ->
                println("===============================")
                println("Variant name: ${variant.name}")
                println("Version name: ${variant.versionName}")
                println("Version code: ${variant.versionCode}")
                println("Flavor name: ${variant.productFlavors.map { it.name }.joinToString()}")
                println("Build directory: ${layout.buildDirectory.get()}")

                val flavorName = variant.productFlavors.map { it.name }.joinToString()
                val variantName = variant.name
                val buildType = variant.buildType.name
                val versionName = variant.versionName
                val versionCode = variant.versionCode

                val bundleDir =
                    "${layout.buildDirectory.get()}/outputs/bundle/${variantName}"

                val oldFileName = "app-${flavorName}-${buildType}.aab"
                val newFileName =
                    "${variant.applicationId}-" +
                            "v$versionName-" +
                            "build$versionCode-" +
                            "$flavorName-" +
                            "$buildType.aab"

                //remove an old file if exist
                delete("$bundleDir/$newFileName")

                val originalFile = file("$bundleDir/$oldFileName")

                if (!originalFile.exists()) {
                    println("Original '${originalFile.absolutePath}' file not found")
                } else {
                    println("newFileName: $newFileName")
                    val newFile = file("$bundleDir/$newFileName")
                    originalFile.renameTo(newFile)
                    println("Renamed '$oldFileName' file to $newFileName")
                }
            }

        }
            ?: println("Android extension not found. This task is only valid for Android application projects.")
    }
}

tasks.matching { task -> task.name.contains("release", ignoreCase = true) }
    .configureEach {
        finalizedBy("renameBundle")
    }