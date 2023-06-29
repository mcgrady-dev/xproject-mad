plugins {
    id("xproject.android.library")
    id("xproject.android.hilt")
}

android {
    namespace = "com.mcgrady.xproject.core.testing"
}

dependencies {

    api(libs.androidx.test.core)
    api(libs.androidx.test.espresso.core)
    api(libs.androidx.test.rules)
    api(libs.androidx.test.runner)
    api(libs.hilt.android.testing)
    api(libs.junit4)
    api(libs.kotlinx.coroutines.test)
    api(libs.turbine)

    implementation(project(":core:common"))
    implementation(libs.kotlinx.datetime)
}