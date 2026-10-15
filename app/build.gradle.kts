plugins {
    id("com.android.application")
    // Plugin de Firebase (necesario para tu inicio de sesión)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.franco.asistenciaiquique"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.franco.asistenciaiquique"
        minSdk = 26
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    // 1. Dependencias base de Android y Diseño
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // 2. Firebase (Autenticación que ya tienes en tu MainActivity)
    // Usamos el BoM de Firebase para que las versiones sean compatibles automáticamente
    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")

    // 3. ¡NUEVO! Librería de mapas gratuitos (Osmdroid)
    implementation("org.osmdroid:osmdroid-android:6.1.18")

    // 4. Testing (por defecto en Android Studio)
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}