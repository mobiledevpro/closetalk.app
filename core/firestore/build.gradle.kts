plugins {
    id("core-module")
}

dependencies {
    implementation(platform(libs.firebase.bom))
    api(libs.firebase.firestore)
}