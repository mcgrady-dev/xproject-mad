plugins {
    id("xproject.android.feature")
}

android {
    resourcePrefix = "pokemon_"

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    namespace = "com.mcgrady.xproject.feature.pokemon.compose"
}

dependencies {

    implementation(project(":data:pokemon"))

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.retrofit.converter.serialization)

    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.graphics)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)

    //test
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.compose.ui.test)
    debugImplementation(libs.compose.ui.tooling)
    debugImplementation(libs.compose.ui.testManifest)
}