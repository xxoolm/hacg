import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.google.devtools.ksp.KspExperimental

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("kotlin-parcelize")
}

android {
    namespace = "io.github.yueeng.hacg"
    compileSdk = 35
    buildToolsVersion = "35.0.0"

    defaultConfig {
        applicationId = "io.github.yueeng.hacg"
        minSdk = 21
        targetSdk = 35
        versionCode = 42
        versionName = "1.5.6"
        resourceConfigurations.add("zh-rCN")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    signingConfigs {
        create("release") {
            val config = gradleLocalProperties(rootDir, providers)
            storeFile = file(config.getProperty("storeFile"))
            storePassword = config.getProperty("storePassword")
            keyAlias = config.getProperty("keyAlias")
            keyPassword = config.getProperty("keyPassword")
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
        }
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    ksp {
        @OptIn(KspExperimental::class)
        useKsp2 = false
    }
    applicationVariants.all {
        if (name != "release") return@all
        outputs.all {
            project.tasks.getByName("assembleRelease").doLast {
                val folder = File(rootDir, "release").also { it.mkdirs() }
                outputFile.parentFile?.listFiles()?.forEach { it.copyTo(File(folder, it.name), true) }
            }
        }
    }
}

dependencies {
    val lifecycleVersion = "2.9.1"
    val glideVersion = "4.16.0"
    val okhttpVersion = "5.1.0"
    val kotlinxCoroutinesVersion = "1.10.2"
    implementation("androidx.core:core-ktx:1.16.0")
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.preference:preference-ktx:1.2.1")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.4.0")
    implementation("androidx.slidingpanelayout:slidingpanelayout:1.2.0")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    implementation("androidx.activity:activity-ktx:1.10.1")
    implementation("androidx.fragment:fragment-ktx:1.8.8")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")
    implementation("androidx.paging:paging-runtime-ktx:3.3.6")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxCoroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$kotlinxCoroutinesVersion")
    implementation("com.github.clans:fab:1.6.4")
    implementation("com.squareup.okhttp3:okhttp:$okhttpVersion")
    implementation("com.squareup.okhttp3:logging-interceptor:$okhttpVersion")
    implementation("com.github.bumptech.glide:glide:$glideVersion")
    implementation("com.github.bumptech.glide:okhttp3-integration:$glideVersion")
    ksp("com.github.bumptech.glide:ksp:$glideVersion")
    implementation("org.jsoup:jsoup:1.21.1")
    implementation("com.google.code.gson:gson:2.13.1")
}