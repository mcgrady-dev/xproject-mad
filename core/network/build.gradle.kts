@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("xproject.android.library")
    id("xproject.android.hilt")
}

android {
    namespace = "com.mcgrady.xproject.core.network"
}

dependencies {
    implementation(project(":core:common"))

    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
    implementation(libs.retrofit)

    debugImplementation(libs.chucker.snapshot)
    releaseImplementation(libs.chucker.snapshot.noop)
}