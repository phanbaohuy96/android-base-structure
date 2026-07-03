plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
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
    namespace = "com.pbh.androidbase.data"
    compileSdk = 36

    defaultConfig {
        minSdk = 24
        consumerProguardFiles("consumer-rules.pro")
    }

    flavorDimensions += "environment"
    productFlavors {
        create("dev") {
            dimension = "environment"
            buildConfigField("Boolean", "USE_MOCK", "true")
            buildConfigField("String", "BASE_URL", "\"${envOrProperty("ANDROID_BASE_URL", "https://dev.example.invalid/")}\"")
        }
        create("staging") {
            dimension = "environment"
            buildConfigField("Boolean", "USE_MOCK", "false")
            buildConfigField("String", "BASE_URL", "\"${envOrProperty("ANDROID_BASE_URL", "https://staging.example.invalid/")}\"")
        }
        create("prod") {
            dimension = "environment"
            buildConfigField("Boolean", "USE_MOCK", "false")
            buildConfigField("String", "BASE_URL", "\"${envOrProperty("ANDROID_BASE_URL", "https://api.example.invalid/")}\"")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        buildConfig = true
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":domain"))
    implementation(libs.androidx.security.crypto)
    implementation(libs.coroutines.android)
    implementation(libs.datastore.preferences)
    implementation(libs.hilt.android)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
    implementation(libs.retrofit)
    implementation(libs.retrofit.kotlinx.serialization.converter)
    implementation(libs.room.ktx)
    implementation(libs.room.runtime)
    ksp(libs.hilt.compiler)
    ksp(libs.room.compiler)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.truth)
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}
