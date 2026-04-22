plugins {
    application
    alias(libs.plugins.lombok)
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(libs.junit.jupyter)
    testRuntimeOnly(libs.junit.platformLauncher)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

application {
    mainClass = "io.github.hhhannahmmmontana.library.App"
}

tasks.run {
    standardInput = System.`in`
}

tasks.test {
    useJUnitPlatform()
}
