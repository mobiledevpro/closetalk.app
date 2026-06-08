import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.google.firebase.crashlytics.buildtools.gradle.CrashlyticsExtension
import java.io.FileNotFoundException
import java.util.Properties

plugins {
    id("com.android.application")
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

            configure<CrashlyticsExtension> {
                mappingFileUploadEnabled = false
            }
        }

        getByName("release") {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = try {
                signingConfigs.getByName("release")
            } catch (e: UnknownDomainObjectException) {
                println("SigningConfig with not found. Skipping...")
                null
            }

            configure<CrashlyticsExtension> {
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
        isCoreLibraryDesugaringEnabled = true
    }


    buildFeatures {
        compose = true
        buildConfig = true
    }

    packaging {
        resources.excludes.add("/META-INF/{AL2.0,LGPL2.1}")
    }
}

kotlin {
    jvmToolchain(libs.versions.jdkVersion.get().toInt())
}


dependencies {

    implementation(libs.androidx.core.ktx)
    coreLibraryDesugaring(libs.desugaring)
    implementation(libs.bundles.lifecycle)

    implementation(projects.core.navigation)

    implementation(libs.firebase.performance)
    implementation(projects.core.analytics)
    implementation(projects.core.database)

    testImplementation(libs.bundles.test.common)

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
    implementation(projects.feature.sync)
}



//This task is used for .AAB file renaming to following format:
//{application_id}-v{version_name}-build{version_code}-{product_flavor}-{build_type}.aab

abstract class RenameBundleTask : DefaultTask() {
    @get:Input
    abstract val variantsData: ListProperty<Map<String, String>>

    @get:Internal
    abstract val buildDirectory: DirectoryProperty

    @TaskAction
    fun taskAction() {
        val bDir = buildDirectory.get().asFile
        val dataList = variantsData.get()

        dataList.forEach { info ->
            val variantName = info["variantName"] ?: return@forEach
            val bundleDir = File(bDir, "outputs/bundle/$variantName")

            if (bundleDir.exists()) {
                val aabFile = bundleDir.listFiles { _, name -> name.endsWith(".aab") }
                    ?.find { it.name.startsWith("app-") }

                if (aabFile != null) {
                    val flavorName = info["flavorName"] ?: ""
                    val buildType = info["buildType"] ?: ""
                    val versionName = info["versionName"] ?: ""
                    val versionCode = info["versionCode"] ?: ""
                    val applicationId = info["applicationId"] ?: ""

                    val newFileName =
                        "$applicationId-v$versionName-build$versionCode${if (flavorName.isNotEmpty()) "-$flavorName" else ""}-$buildType.aab"
                            .replace("--", "-")

                    val targetFile = File(bundleDir, newFileName)

                    if (targetFile.exists()) {
                        targetFile.delete()
                    }

                    if (aabFile.renameTo(targetFile)) {
                        logger.lifecycle("Successfully renamed ${aabFile.name} to $newFileName")
                    } else {
                        logger.error("Error: Failed to rename ${aabFile.name} to $newFileName")
                    }
                }
            }
        }
    }
}

val renameBundle = tasks.register<RenameBundleTask>("renameBundle") {
    buildDirectory.set(layout.buildDirectory)
}

val androidComponents = extensions.findByType<ApplicationAndroidComponentsExtension>()

androidComponents?.onVariants { variant ->
    val appId = variant.applicationId.get()
    val variantName = variant.name
    val buildType = variant.buildType
    val flavorName = variant.flavorName ?: ""

    variant.outputs.forEach { output ->
        renameBundle.configure {
            variantsData.add(
                mapOf(
                    "variantName" to variantName,
                    "applicationId" to appId,
                    "versionName" to output.versionName.get(),
                    "versionCode" to output.versionCode.get().toString(),
                    "buildType" to (buildType ?: ""),
                    "flavorName" to flavorName
                )
            )
        }
    }
}

tasks.matching { task ->
    task.name.startsWith("bundle") && task.name.contains("Release", ignoreCase = true)
}.configureEach {
    finalizedBy(renameBundle)
}