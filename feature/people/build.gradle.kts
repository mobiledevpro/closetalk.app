plugins {
    id("feature-module")
}

dependencies {
    api(projects.feature.peopleList)
    api(projects.feature.peopleProfile)
}