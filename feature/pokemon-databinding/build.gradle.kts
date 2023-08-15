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
    id("xproject.android.glide")
    id("kotlinx-serialization")
}

android {
    resourcePrefix = "pokemon_"

    buildFeatures {
        dataBinding = true
    }

    namespace = "com.mcgrady.xproject.feature.pokemon.databinding"
}

dependencies {

    implementation(project(":core:ui"))
    implementation(project(":data:pokemon"))

    implementation(libs.androidx.activity)
    implementation(libs.androidx.palette.ktx)
    implementation(libs.flexbox)

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

    //test
    testImplementation(libs.okhttp.mocks)
    testImplementation(libs.truth)
}