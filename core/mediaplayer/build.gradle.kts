
plugins {
    id("xproject.android.library")
    id("xproject.android.hilt")
    id("xproject.android.room")
    id("kotlin-parcelize")
}

android {
    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }

    namespace = "com.mcgrady.xproject.core.mediaplayer"
}

dependencies {

    implementation(project(":core:ui"))
    implementation(project(":core:common"))
    implementation(project(":core:network"))
}