/*
 * Copyright 2022 mcgrady
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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