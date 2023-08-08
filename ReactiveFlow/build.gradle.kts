import org.jetbrains.kotlin.konan.properties.Properties
import java.io.File
import java.io.FileInputStream

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id("maven-publish")
    id("signing")
    kotlin("kapt")
}

val sourcesJar = tasks.register("sourcesJar", Jar::class) {
    from(android.sourceSets["main"].java.getSourceFiles())
    archiveClassifier.set("sources")
}

val javadoc = tasks.register("javadoc", Javadoc::class) {
    source = android.sourceSets["main"].java.getSourceFiles()
    classpath += project.files(android.bootClasspath)
}

val javadocJar = tasks.register("javadocJar", Jar::class) {
    dependsOn(javadoc)
    from(javadoc)
    archiveClassifier.set("javadoc")
}

android {
    namespace = "io.github.smmousavi8872.reactiveflow"
    compileSdk = 33

    defaultConfig {
        minSdk = 16

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    kotlin {
        jvmToolchain(8)
    }
}

publishing {
    publications {
        afterEvaluate {
            create<MavenPublication>("lib-publish") {
                from(components["release"])

                artifact(sourcesJar)
                artifact(javadocJar)

                groupId = "io.github.smmousavi8872.reactiveflow"
                artifactId = "reactive-flow"
                version = "1.0.1.6"

                pom {
                    name.set("ReactiveFlow")
                    packaging = "arr"
                    description.set("A library to mimic the behavior of Rx extension using coroutine SharedFlow")
                    url.set("https://github.com/smmousavi8872/ReactiveFlow")

                    licenses {
                        license {
                            name.set("The Apache License, Version 2.0")
                            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                            distribution.set("repo")
                        }
                    }

                    developers {
                        developer {
                            id.set("smmousavi8872")
                            name.set("Mohsen Mousavi")
                            url.set("https://github.com/smmousavi8872")
                            email.set("s.m.mousavi1993@gmail.com")
                        }
                    }

                    scm {
                        connection.set("scm:git@github.com:smmousavi8872/ReactiveFlow.git")
                        developerConnection.set("scm:git@github.com:smmousavi8872/ReactiveFlow.git")
                        url.set("https://github.com/smmousavi8872/ReactiveFlow/tree/master")
                    }
                }
            }
        }
    }

    repositories {
        maven {
            name = "OSSRH"
            url =
                uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2")
            val properties = Properties().apply {
                load(FileInputStream(File(rootProject.rootDir, "local.properties")))
            }
            credentials {
                username = properties.getProperty("mavenCentralUsername")
                password = properties.getProperty("mavenCentralPassword")
            }

        }
    }
}

signing {
    useGpgCmd()
    afterEvaluate {
        sign(publishing.publications["lib-publish"])
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    // hilt
    implementation("com.google.dagger:hilt-android:2.47")
    kapt("com.google.dagger:hilt-android-compiler:2.47")
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}