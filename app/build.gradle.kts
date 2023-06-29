@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("xproject.android.application")
//    id("xproject.android.application.flavors")
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
    implementation(project(":feature:pokemon"))
    implementation(project(":feature:zhihu"))

    implementation(project(":core:common"))
    implementation(project(":core:ui"))
    implementation(project(":core:network"))

    //router
    implementation(libs.therouter.router)
    kapt(libs.therouter.apt)

    implementation(libs.androidx.activity)
    implementation(libs.androidx.coordinatorlayout)
    implementation(libs.material)

    debugImplementation(libs.leakcanary)
    debugImplementation(libs.blockcanary)
    releaseImplementation(libs.blockcanary.noop)
}