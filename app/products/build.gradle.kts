plugins {
    kotlin("jvm") version "2.0.21"
}

group = "io.github.andersori"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":utils"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
    testImplementation(kotlin("test"))
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0")
}

tasks.test {
    useJUnitPlatform()
    jvmArgs = listOf("-Xshare:off")
}
kotlin {
    jvmToolchain(17)
}