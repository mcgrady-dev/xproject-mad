plugins {
    id("xproject.android.feature")
}

android {
    namespace = "com.mcgrady.xproject.feature.account"

    viewBinding {
        enable = true
    }
}

dependencies {

    implementation(project(":core:network"))

    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    implementation(libs.moshi)
    implementation(libs.retrofit.moshi)

    testImplementation(libs.mockk)
    testImplementation(libs.truth)
}
