@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("xproject.android.feature")
}

android {
    namespace = "com.mcgrady.xproject.zhihu"
}

dependencies {

    implementation(project(":core:network"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
}
