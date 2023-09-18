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

plugins {
    id("xproject.android.library")
    id("xproject.android.hilt")
}

android {
    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }
    buildFeatures {
        buildConfig = true
    }

    namespace = "com.mcgrady.xproject.core.common"
}

dependencies {
    api(libs.core.ktx)
    api(libs.appcompat)
    api(libs.androidx.activity)

    api(libs.xlabs.viewbinding) { isChanging = true }
    api(libs.xlabs.core.ktx) { isChanging = true }
    api(libs.utilcodex)
    api(libs.timber)
    api(libs.lifecycle.livedata.ktx)
    api(libs.lifecycle.viewModel.ktx)
    api(libs.mmkv)

}
