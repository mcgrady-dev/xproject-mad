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
    id("xproject.android.application")
    id("xproject.android.hilt")
}

android {
    defaultConfig {
        applicationId = "com.mcgrady.xproject"
        versionCode = 1
        versionName = "0.0.1"
    }

    viewBinding {
        enable = true
    }

//    signingConfigs {
//        release {
//            keyAlias keystoreProperties['releaseKeyAlias']
//            keyPassword keystoreProperties['releaseKeyPassword']
//            storeFile file(keystoreProperties['releaseStoreFile'])
//            storePassword keystoreProperties['releaseStorePassword']
//        }
//        debug {
//            keyAlias keystoreProperties['debugKeyAlias']
//            keyPassword keystoreProperties['debugKeyPassword']
//            storeFile file(keystoreProperties['debugStoreFile'])
//            storePassword keystoreProperties['debugStorePassword']
//        }
//    }

    buildTypes {
        debug {
//            applicationIdSuffix = NiaBuildType.DEBUG.applicationIdSuffix
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        release {
//            applicationIdSuffix = NiaBuildType.RELEASE.applicationIdSuffix
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
    namespace = "com.mcgrady.xproject"
}

dependencies {

    implementation(project(":feature:account"))
    implementation(project(":feature:pokemon-databinding"))
    implementation(project(":feature:pokemon-viewbinding"))
    implementation(project(":feature:pokemon-compose"))
    implementation(project(":feature:zhihu"))
    implementation(project(":feature:musicplayer"))

    implementation(project(":core:common"))
    implementation(project(":core:ui"))
    implementation(project(":core:network"))

    implementation(libs.splashscreen)

    debugImplementation(libs.leakcanary)
//    debugImplementation(libs.blockcanary)
//    releaseImplementation(libs.blockcanary.noop)
}