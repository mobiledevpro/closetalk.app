plugins {
    id("core-module")
    id("com.google.devtools.ksp")
}

val databaseVersion: String = libs.versions.app.database.version.get()
val databaseName: String = libs.versions.app.database.name.get()
android.defaultConfig {
    buildConfigField("int", "DB_VERSION", databaseVersion)
    buildConfigField("String", "DB_NAME", "\"$databaseName\"")

    ksp {
        arg("room.schemaLocation", "$projectDir/schemas")
    }
}

android.buildFeatures.buildConfig = true

dependencies {
    api(libs.room)
    ksp(libs.room.compiler)
    implementation(libs.room.ktx)
}
