plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
}

dependencies {
    implementation(project(":composeApp"))
    implementation(compose.desktop.currentOs)
}

compose.desktop {
    application {
        mainClass = "com.fernando.ds.desktop.MainKt"
    }
}