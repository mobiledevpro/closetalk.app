@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.compose.compiler) apply false

    // To generate a module graph
    alias(libs.plugins.module.graph)
}

tasks.register("clean", Delete::class){
    delete(layout.buildDirectory.get())
}

// To generate the Compose compiler metrics. Files generated in the /build/compose_metrics directory
// Article https://proandroiddev.com/optimize-app-performance-by-mastering-stability-in-jetpack-compose-69f40a8c785d

// Interpreting Compose Compiler Metrics https://github.com/JetBrains/kotlin/blob/master/plugins/compose/design/compiler-metrics.md
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

moduleGraphConfig {
    readmePath.set("./README.md")
    heading = "## Module Graph"
}