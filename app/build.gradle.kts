plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.google.ksp) // Changed to use version catalog alias
}

android {
    namespace = "com.example.Aqi"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.Aqi"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            isShrinkResources = false
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
//    kotlinOptions {
//        jvmTarget = "11"
//    }

    // NEW CODE
    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")

//ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.10.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.10.0")

// Room
    implementation("androidx.room:room-runtime:2.8.4")
    implementation("androidx.room:room-ktx:2.8.4")
    ksp("androidx.room:room-compiler:2.8.4")

//Retrofit
// For Retrofit (our "waiter" for making network calls)
    implementation("com.squareup.retrofit2:retrofit:3.0.0")

//HttpLoggingInterceptor
    implementation("com.squareup.okhttp3:logging-interceptor:5.3.2")

// For GSON (to automatically convert the server's JSON response into our Kotlin data classes)
    implementation("com.squareup.retrofit2:converter-gson:3.0.0")


    //Lottie
    implementation("com.airbnb.android:lottie-compose:6.7.1")


    //Google fonts
    implementation("androidx.compose.ui:ui-text-google-fonts:1.10.3")

    //Material theme expressive(BOTH)
    implementation("androidx.compose.material:material:1.10.3")
    implementation("androidx.compose.material3:material3:1.5.0-alpha14")

    // Material icons for Compose
    implementation("androidx.compose.material:material-icons-extended:1.7.8")

    //DataStore
    implementation("androidx.datastore:datastore-preferences:1.2.1")

    implementation("androidx.security:security-crypto:1.1.0")

    implementation("com.google.crypto.tink:tink-android:1.20.0") // Or the latest stable version

    implementation("com.jakewharton.timber:timber:5.0.1")

    implementation("io.insert-koin:koin-android:4.2.0")
    implementation("io.insert-koin:koin-androidx-compose:4.2.0")

    //KTor
    implementation("io.ktor:ktor-client-core:3.5.1")

    implementation("io.ktor:ktor-client-okhttp:3.5.1")

    implementation("io.ktor:ktor-client-content-negotiation:3.5.1")

    implementation("io.ktor:ktor-serialization-kotlinx-json:3.5.1")

    implementation("io.ktor:ktor-client-logging:3.5.1")

    implementation("io.ktor:ktor-client-auth:3.4.3")

    // Kotlinx Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")

    implementation(libs.google.play.services.location)

    implementation("androidx.navigation3:navigation3-runtime:1.1.4")
//    implementation("androidx.navigation3:navigation3-compose:1.1.4")
    implementation("androidx.navigation3:navigation3-ui:1.1.4")

    // Google Maps Compose
    implementation("com.google.maps.android:maps-compose:6.4.1")
    implementation("com.google.android.gms:play-services-maps:19.0.0")
    implementation("com.google.android.gms:play-services-location:21.3.0")

    //ExoPlayer
    implementation("androidx.media3:media3-exoplayer:1.10.1")
    implementation("androidx.media3:media3-ui:1.10.1")
}
