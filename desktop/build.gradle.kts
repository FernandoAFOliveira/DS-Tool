plugins {
    application
    id("org.openjfx.javafxplugin") version "0.1.0"
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

javafx {
    version = "21.0.11"
    modules = listOf("javafx.controls", "javafx.web", "javafx.swing")
}

application {
    mainClass.set("com.fernando.ds.GuiMain")
    applicationDefaultJvmArgs = listOf(
        "--enable-native-access=javafx.graphics,javafx.web"
    )
}

dependencies {
    implementation(project(":core"))

    testImplementation(platform("org.junit:junit-bom:5.11.4"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}