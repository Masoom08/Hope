plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "st.masoom.hope"
    compileSdk = 35

    defaultConfig {
        applicationId = "st.masoom.hope"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.firebase.auth)
    implementation(libs.play.services.fido)
    implementation(libs.androidx.espresso.core)
    implementation(libs.firebase.firestore)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation("androidx.navigation:navigation-compose:2.8.4")

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")

    implementation("androidx.compose.runtime:runtime-livedata:1.7.5") // For using LiveData with Compose
    implementation("com.google.android.gms:play-services-location:21.3.0")
    implementation("com.google.android.gms:play-services-maps:19.0.0")
    implementation("com.google.maps.android:maps-compose:2.11.4")
    implementation("com.google.accompanist:accompanist-permissions:0.31.5-beta")
    implementation("com.google.android.gms:play-services-auth:21.2.0")
    implementation("com.google.android.gms:play-services-safetynet:18.1.0")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")


    // Identity Services (Required for One Tap Sign-In)
    implementation ("com.google.android.gms:play-services-identity:18.0.1")

    implementation("io.coil-kt:coil-compose:2.5.0")

    implementation("com.google.firebase:firebase-auth-ktx:22.3.1")

    implementation("com.google.firebase:firebase-storage-ktx:20.3.0")
    implementation("androidx.activity:activity-compose:1.8.2") // Required for file picking
    /*
    implementation("com.google.firebase:firebase-firestore-ktx:24.10.0"){
        exclude(group = "com.google.firebase", module = "firebase-common")
    }

     */

    //implementation("org.webrtc:google-webrtc:1.0.+")
    //implementation("io.github.piasy:WebRTC:69.1.2")
    //implementation(files("libs/google-webrtc.aar"))


    implementation ("androidx.compose.ui:ui:1.6.1")
}