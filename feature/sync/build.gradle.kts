plugins {
    id("feature-module")
}

dependencies {
    implementation(projects.core.firestore)
    implementation(projects.core.database)
}