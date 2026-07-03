import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties =
    Properties().apply {
        if (keystorePropertiesFile.exists()) {
            keystorePropertiesFile.inputStream().use(::load)
        }
    }

fun envOrProperty(
    name: String,
    fallback: String,
): String =
    providers
        .gradleProperty(name)
        .orElse(providers.environmentVariable(name))
        .orElse(fallback)
        .get()

android {
    namespace = "com.pbh.androidbase"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.pbh.androidbase"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "0.1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            if (keystoreProperties.isNotEmpty()) {
                storeFile = file(keystoreProperties["storeFile"] as String)
                storePassword = keystoreProperties["storePassword"] as String
                keyAlias = keystoreProperties["keyAlias"] as String
                keyPassword = keystoreProperties["keyPassword"] as String
            }
        }
    }

    flavorDimensions += "environment"
    productFlavors {
        create("dev") {
            dimension = "environment"
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"
            buildConfigField("Boolean", "USE_MOCK", "true")
            buildConfigField("String", "BASE_URL", "\"${envOrProperty("ANDROID_BASE_URL", "https://dev.example.invalid/")}\"")
        }
        create("staging") {
            dimension = "environment"
            applicationIdSuffix = ".staging"
            versionNameSuffix = "-staging"
            buildConfigField("Boolean", "USE_MOCK", "false")
            buildConfigField("String", "BASE_URL", "\"${envOrProperty("ANDROID_BASE_URL", "https://staging.example.invalid/")}\"")
        }
        create("prod") {
            dimension = "environment"
            buildConfigField("Boolean", "USE_MOCK", "false")
            buildConfigField("String", "BASE_URL", "\"${envOrProperty("ANDROID_BASE_URL", "https://api.example.invalid/")}\"")
        }
    }

    buildTypes {
        debug {
            isDebuggable = true
        }
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            if (keystoreProperties.isNotEmpty()) {
                signingConfig = signingConfigs.getByName("release")
            }
        }
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(project(":feature-auth"))
    implementation(project(":feature-home"))
    implementation(platform(libs.compose.bom))
    implementation(libs.activity.compose)
    implementation(libs.compose.material3)
    implementation(libs.compose.ui)
    implementation(libs.hilt.android)
    implementation(libs.navigation.compose)
    ksp(libs.hilt.compiler)
    debugImplementation(libs.compose.ui.tooling)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.compose.ui.test.junit4)
}
