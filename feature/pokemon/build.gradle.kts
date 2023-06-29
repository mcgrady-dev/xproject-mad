//val buildModule: String by project
//val isBuildModule: Boolean = buildModule.toBoolean()
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("xproject.android.feature")
    id("kotlinx-serialization")
    id("kotlin-parcelize")
    id("xproject.android.room")
    id("xproject.android.glide")
//    id("therouter")
}

android {
    resourcePrefix = "pokemon_"

    buildFeatures {
        dataBinding = true
        viewBinding = true
    }

    namespace = "com.mcgrady.xproject.feature.pokemon"
}

dependencies {

    implementation(project(":core:network"))

    implementation(libs.androidx.activity)
    implementation(libs.material)
    implementation(libs.androidx.palette.ktx)

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.retrofit.kotlin.serialization)

//    //glide
//    implementation(libs.glide)
//    implementation(libs.glide.okhttp) {
//        exclude(group = "com.squareup.okhttp3", module = "okhttp")
//    }
//    implementation(libs.glide.recyclerview) {
//        // Excludes the support library because it's already included by Glide.
//        isTransitive = false
//    }
////    kapt(libs.glide.compiler)
//    ksp(libs.glide.ksp)

    implementation(libs.flexbox)

    //test
    testImplementation(libs.okhttp.mocks)
    testImplementation(libs.truth)
}