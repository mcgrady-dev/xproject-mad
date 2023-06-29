
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class AndroidGlideConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.google.devtools.ksp")

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            dependencies {
                add("implementation", libs.findLibrary("glide").get())
                add("implementation", libs.findLibrary("glide.okhttp").get())
                add("implementation", libs.findLibrary("glide.recyclerview").get())
                add("ksp", libs.findLibrary("glide.ksp").get())
//                add("implementation", libs.findBundle("glide").get())
            }
        }
    }
}