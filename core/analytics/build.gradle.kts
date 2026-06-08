plugins {
    id("core-module")
}

dependencies {
    api(platform(libs.firebase.bom))
    api(libs.firebase.analytics)
    api(libs.firebase.crashlytics)
    api(libs.firebase.performance)
}