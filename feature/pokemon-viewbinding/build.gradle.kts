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
    alias(libs.plugins.kotlin.android)
//    id("xproject.android.room")
//    id("xproject.android.glide")
//    id("therouter")
}

android {
    resourcePrefix = "pokemon_"

    buildFeatures {
        viewBinding = true
    }

    namespace = "com.mcgrady.xproject.feature.pokemon.viewbinding"
}

dependencies {

    implementation(project(":data:pokemon"))

    implementation(libs.androidx.activity)
    implementation(libs.material)
    implementation(libs.androidx.palette.ktx)

    implementation(libs.flexbox)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
}