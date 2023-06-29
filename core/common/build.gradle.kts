
@Suppress("DSL_SCOPE_VIOLATION")
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
    api(libs.xarch.snapshot) {
        isChanging = true
    }

    api(libs.utilcodex)
    api(libs.timber)
    api(libs.androidx.lifecycle.livedata.ktx)
    api(libs.androidx.lifecycle.viewModel.ktx)

}
