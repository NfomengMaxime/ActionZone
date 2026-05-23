plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.actionzone.app"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.actionzone.app"
        minSdk = 24
        targetSdk = 36
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
}

dependencies {
    // Bibliothèques de base Android — écrites en chaîne directe pour éviter les erreurs libs.xxx
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.activity:activity:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // Glide : charge les affiches de films depuis une URL internet et les affiche dans un ImageView
    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")

    // RecyclerView : widget pour afficher une longue liste de films qui défile verticalement
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // CardView : chaque film dans la liste sera dans une carte avec coins arrondis et ombre
    implementation("androidx.cardview:cardview:1.0.0")

    // Tests — obligatoires, on ne touche pas à ça
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}