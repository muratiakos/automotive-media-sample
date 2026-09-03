import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("com.android.application")
    id("com.android.legacy-kapt")
}

android {
    compileSdk = 37

    defaultConfig {
        applicationId = "com.volvocars.otaevent"
        minSdk = 29
        targetSdk = 37
        versionCode = 5
        versionName = "0.5"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    namespace = "com.volvocars.mediasample"
}

val kotlin_version: String by rootProject.extra
val glide_version: String by rootProject.extra
val media3_version: String by rootProject.extra
val koin_version: String by rootProject.extra
val junit_version: String by rootProject.extra

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version")

    implementation("androidx.core:core-ktx:1.19.0")
    implementation("androidx.appcompat:appcompat:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.2")
    implementation("androidx.media:media:1.8.0")
    implementation("com.github.bumptech.glide:glide:$glide_version")
    //kapt("com.github.bumptech.glide:compiler:$glide_version")

    // Media3
    api("androidx.media3:media3-exoplayer:$media3_version")
    api("androidx.media3:media3-ui:$media3_version")
    api("androidx.media3:media3-common:$media3_version")
    api("androidx.media3:media3-session:$media3_version")

    // DI
    implementation("io.insert-koin:koin-core:$koin_version")
    implementation("io.insert-koin:koin-android:4.2.2")

    testImplementation("junit:junit:$junit_version")
    testImplementation("androidx.test:core:1.7.0")
    testImplementation("io.insert-koin:koin-test:$koin_version")
}

repositories {
    mavenCentral()
}
