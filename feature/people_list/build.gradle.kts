plugins {
    id("feature-module")
    id("testing-module")
}

dependencies {
    implementation(projects.core.database)
}