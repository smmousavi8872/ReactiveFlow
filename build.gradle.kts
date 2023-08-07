// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.0" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
    id("com.android.library") version "8.1.0" apply false
    id("com.google.dagger.hilt.android") version "2.47" apply false
}

buildscript {
    val kotlinVersion = "1.8.10"
    repositories {
        google()
        mavenCentral() // NEW
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.1.3")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")

        classpath("com.vanniktech:gradle-maven-publish-plugin:0.20.0") // NEW
        classpath("org.jetbrains.dokka:dokka-gradle-plugin:1.6.10") // NEW
    }
}