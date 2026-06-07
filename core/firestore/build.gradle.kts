plugins {
    id("core-module")
}

dependencies {
    api(platform(libs.firebase.bom))
    api(libs.firebase.firestore)
}