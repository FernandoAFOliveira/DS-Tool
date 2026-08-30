plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
}

dependencies {
    implementation(project(":composeApp"))
    implementation(compose.desktop.currentOs)
}

// The Java tests under desktop/src/test/java belong to the retired Swing/Mermaid
// desktop implementation. They reference classes that are no longer part of this
// Compose launcher module. Keep them in the repository for migration review, but
// do not compile them as tests for the new desktop launcher.
sourceSets {
    test {
        java.setSrcDirs(emptyList<String>())
    }
}

compose.desktop {
    application {
        mainClass = "com.fernando.ds.desktop.MainKt"
    }
}
