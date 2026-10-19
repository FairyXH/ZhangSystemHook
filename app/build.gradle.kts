import java.util.Properties

plugins {
    id("com.android.application")
}

abstract class FilterResourcesTask : DefaultTask() {
    @get:Internal abstract val sourceDirectory: DirectoryProperty
    @get:OutputDirectory abstract val outputDirectory: DirectoryProperty
    @get:InputFiles
    @get:PathSensitive(PathSensitivity.RELATIVE)
    val resources: FileTree get() = sourceDirectory.asFileTree.matching { exclude("**/*.baiduyun.*") }
    @TaskAction fun stage() {
        project.sync {
            from(resources)
            into(outputDirectory)
            includeEmptyDirs = false
        }
    }
}

val filteredResources = tasks.register<FilterResourcesTask>("filterMainResources") {
    sourceDirectory.set(layout.projectDirectory.dir("src/main/res"))
    outputDirectory.set(layout.buildDirectory.dir("generated/filteredMainResources"))
}

val savedVersion = Properties().apply {
    rootProject.file("version.properties").takeIf { it.exists() }?.inputStream()?.use { load(it) }
}.getProperty("VERSION_CODE", "1").toInt()

providers.gradleProperty("buildOutputRoot").orNull?.let { layout.buildDirectory.set(file("$it/app")) }

android {
    namespace = "io.github.fairyxh.ZhangSystemHook"
    compileSdk = 37
    defaultConfig {
        applicationId = "io.github.fairyxh.ZhangSystemHook"
        minSdk = 27
        targetSdk = 35
        versionCode = savedVersion
        versionName = "${savedVersion / 100 + 1}.${savedVersion / 10 % 10}.${savedVersion % 10}"
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures { buildConfig = true }
    sourceSets.named("main") { res.setSrcDirs(emptyList<String>()) }
    packaging.resources.merges += "META-INF/xposed/*"
    androidResources.ignoreAssetsPattern = "!.svn:!.git:!.ds_store:!*.scc:.*:CVS:thumbs.db:picasa.ini:*~:*.baiduyun.*"
    lint { checkReleaseBuilds = true }
}

androidComponents.onVariants { variant ->
    variant.sources.res?.addGeneratedSourceDirectory(filteredResources) { it.outputDirectory }
}
dependencies {
    compileOnly("io.github.libxposed:api:102.0.0")
    implementation("io.github.libxposed:service:102.0.0")
    testImplementation("junit:junit:4.13.2")
    testImplementation("io.github.libxposed:api:102.0.0")
}

kotlin { compilerOptions { jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17) } }
