plugins {
    alias(libs.plugins.androidApplication)
}

android {
    namespace = "com.example.carrental"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.carrental"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Retrofit https://square.github.io/retrofit/ - latest version https://github.com/square/retrofit.
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    // Gson -> json data to java or kotlin format
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.1")

    // RecyclerView
    implementation("androidx.recyclerview:recyclerview:1.2.1")
    

}
