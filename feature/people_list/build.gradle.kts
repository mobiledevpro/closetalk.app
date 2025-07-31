plugins {
    id("feature-module")
    id("testing-module")
}

dependencies {
    implementation(projects.feature.peopleCore)
}