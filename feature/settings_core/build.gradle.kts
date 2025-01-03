plugins {
    id("feature-module")
    alias(libs.plugins.google.protobuf)
}


dependencies {
    api(libs.data.store)
    api(libs.protobuf.kotlin)
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:${libs.versions.protobuf.get()}"
    }

    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                create("java") {
                    option("lite")
                }
                /*
                                create("kotlin") {
                                    option("lite")
                                }

                 */
            }
        }
    }

}