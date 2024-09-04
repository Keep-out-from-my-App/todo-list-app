import com.android.build.gradle.BaseExtension
import org.gradle.api.Project
import java.io.File
import java.util.Properties

fun BaseExtension.baseAndroidConfig(project: Project) {
    setCompileSdkVersion(AndroidConst.COMPILE_SKD)

    defaultConfig {
        minSdk = AndroidConst.MIN_SKD
        targetSdk = AndroidConst.COMPILE_SKD

        vectorDrawables {
            useSupportLibrary = true
        }

        testInstrumentationRunner("ru.gribbirg.todoapp.app.TestRunner")
    }

    signingConfigs {
        create("release-signed") {
            val properties = Properties()
            properties.load(project.rootProject.file("secrets.properties").inputStream())

            storeFile = File(properties.getProperty("KEY_STORE_PATH"))
            storePassword = properties.getProperty("KEY_STORE_PASSWORD")
            keyAlias = properties.getProperty("KEY_ALIAS")
            keyPassword = properties.getProperty("KEY_PASSWORD")
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        create("release-signed") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release-signed")
        }
    }

    compileOptions {
        sourceCompatibility = AndroidConst.COMPILE_JDK_VERSION
        targetCompatibility = AndroidConst.COMPILE_JDK_VERSION
    }

    kotlinOptions {
        jvmTarget = AndroidConst.KOTLIN_JVM_TARGET
    }

    packagingOptions {
        resources.excludes.add("META-INF/*")
    }
}

