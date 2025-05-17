import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    kotlin("plugin.serialization") version "1.9.0"
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    jvm("desktop")
    js("wasmJs", IR) {
        browser {
            binaries.executable()
            commonWebpackConfig {
                outputFileName = "wasmJs.js"
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.components.resources)
                implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.3")
                implementation("org.jetbrains.compose.web:web-core:1.6.0")
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(libs.androidx.activity.compose)
                implementation(libs.androidx.lifecycle.runtimeCompose)
                implementation(libs.androidx.lifecycle.viewmodel)
                implementation("io.ktor:ktor-client-core:2.3.3")
                implementation("io.ktor:ktor-client-cio:2.3.3")
                implementation("io.ktor:ktor-client-content-negotiation:2.3.3")
                implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.3")
                implementation("io.coil-kt:coil-compose:2.4.0")
                implementation("androidx.compose.material:material-icons-extended:1.5.1")

                implementation("androidx.compose.material:material-icons-core:1.5.1")
            }
        }

        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation("io.ktor:ktor-client-core:2.3.3")
                implementation("io.ktor:ktor-client-cio:2.3.3")
                implementation("io.ktor:ktor-client-content-negotiation:2.3.3")
                implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.3")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.7.3")

                implementation("org.jetbrains.compose.ui:ui-graphics")
                implementation("io.github.qdsfdhvh:image-loader:1.5.1")
                implementation(compose.material3)
            }
        }

        val wasmJsMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-js:2.3.4")
                implementation("io.ktor:ktor-client-content-negotiation:2.3.3")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
                implementation("org.jetbrains.compose.components:components-resources:1.6.1")
                implementation("org.jetbrains.compose.web:web-core:1.6.0")

                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.web.core)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}

android {
    namespace = "org.example.pokedexmultiplataforma"
    compileSdk = 35

    defaultConfig {
        applicationId = "org.example.pokedexmultiplataforma"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

compose.desktop {
    application {
        mainClass = "org.example.pokedexmultiplataforma.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "org.example.pokedexmultiplataforma"
            packageVersion = "1.0.0"
        }
    }
}
