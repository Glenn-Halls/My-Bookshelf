plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.10"
    id("com.google.devtools.ksp")
    id("com.google.protobuf") version "0.9.1"
}

android {
    namespace = "com.example.mybookshelf"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.mybookshelf"
        minSdk = 30
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.7"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    //noinspection GradleDependency core-ktx version 1.10.1 not compatable with other plugins
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.7.2")
    // Imports the Compose BOM to manage all other aspects of the Compose dependencies
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    // Window Class Size
    implementation("androidx.compose.material3:material3-window-size-class")
    // Compose View Model
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")
    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    // Retrofit with Scalar Converter
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")
    // Retrofit with Kotlin Serialization Converter
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    // Kotlin Serialization dependency
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
    // Coil Library
    implementation("io.coil-kt:coil-compose:2.4.0")
    // Room
    implementation("androidx.room:room-ktx:${rootProject.extra["room_version"]}")
    implementation("androidx.room:room-runtime:${rootProject.extra["room_version"]}")
    // Extended Material Icon Library
    implementation("androidx.compose.material:material-icons-extended:1.5.1")
    ksp("androidx.room:room-compiler:${rootProject.extra["room_version"]}")
    // Compose Rating Bar
    implementation("com.github.a914-gowtham:compose-ratingbar:1.3.6")
    // DataStore Library
    implementation("androidx.datastore:datastore:1.0.0")
    // Protobuf Library
    implementation("com.google.protobuf:protobuf-javalite:3.19.4")
    // WorkManager
    implementation("androidx.work:work-runtime-ktx:2.8.1")
    // J Unit test implementation
    testImplementation("junit:junit:4.13.2")
    // Kotlin CoRoutines test implementation
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.1")
    // Konsist Dependency
    testImplementation("com.lemonappdev:konsist:0.12.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    // Compose BOM test implementation
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}

// Protobuf artifact definition
protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.21.7"
    }
    // Generate proto classes using javalite plugin
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                val java by registering {
                    option("lite")
                }
            }
        }
    }
}
