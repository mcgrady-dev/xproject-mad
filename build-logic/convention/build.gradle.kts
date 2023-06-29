import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

group = "com.mcgrady.xproject.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "xproject.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = "xproject.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidFeature") {
            id = "xproject.android.feature"
            implementationClass = "AndroidFeatureConventionPlugin"
        }
        register("androidTest") {
            id = "xproject.android.test"
            implementationClass = "AndroidTestConventionPlugin"
        }
        register("androidHilt") {
            id = "xproject.android.hilt"
            implementationClass = "AndroidHiltConventionPlugin"
        }
        register("androidRoom") {
            id = "xproject.android.room"
            implementationClass = "AndroidRoomConventionPlugin"
        }
        register("androidGlide") {
            id = "xproject.android.glide"
            implementationClass = "AndroidGlideConventionPlugin"
        }
//        register("androidFlavors") {
//            id = "xproject.android.application.flavors"
//            implementationClass = "AndroidApplicationFlavorsConventionPlugin"
//        }
        register("jvmLibrary") {
            id = "xproject.jvm.library"
            implementationClass = "JvmLibraryConventionPlugin"
        }
    }
}