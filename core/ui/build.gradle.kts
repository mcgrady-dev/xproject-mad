plugins {
    id("xproject.android.library")
    id("kotlin-parcelize")
}

android {
    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }

    namespace = "com.mcgrady.xproject.core.ui"
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.recyclerview)
    implementation(libs.material)
}
