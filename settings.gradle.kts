pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://oss.jfrog.org/libs-snapshot")
        maven(url = "https://oss.sonatype.org/content/repositories/snapshots/")
        maven(url = "https://s01.oss.sonatype.org/content/groups/staging/")
        maven(url = "https://s01.oss.sonatype.org/content/repositories/snapshots/")
    }
}

rootProject.name = "xproject-mad"
include(":app")
include(":core:common")
include(":core:ui")
include(":core:network")
include(":core:testing")

include(":feature:account")
include(":feature:pokemon")
include(":feature:zhihu")
