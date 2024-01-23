group = AppConfig.group
version = AppConfig.versionName

plugins {
    kotlin("jvm") version Dependencies.Versions.kotlinPluginVersion
    id("io.ktor.plugin") version Dependencies.Versions.ktorPluginVersion
    id("com.github.johnrengelman.shadow") version Dependencies.Versions.shadowJarVersion
}

application {
    mainClass.set("com.batcuevasoft.ApplicationKt")
}

ktor {
    fatJar {
        archiveFileName.set("KtorEasy.jar")
    }
}

repositories {
    mavenCentral()
}


dependencies {
//    implementation(kotlin("stdlib-jdk8"))
    implementation(Dependencies.ktorNetty)
    implementation(Dependencies.ktorAuth)
    implementation(Dependencies.ktorJwt)
    implementation(Dependencies.ktorContentNegotiation)
    implementation(Dependencies.ktorSerialization)
    implementation(Dependencies.ktorLogging)
    implementation(Dependencies.ktorStatusPages)
    implementation(Dependencies.ktorMetricsMicrometer)
    implementation(Dependencies.logback)
    implementation(Dependencies.exposedCore)
    implementation(Dependencies.exposedDao)
    implementation(Dependencies.exposedJdbc)
    implementation(Dependencies.hikari)
    implementation(Dependencies.h2)
    implementation(Dependencies.postgresConnector)

    implementation(Dependencies.bcrypt)
    implementation(Dependencies.micrometerPrometeus)

    // Koin for Kotlin
    implementation(Dependencies.koin)
    implementation("io.ktor:ktor-client-jetty:2.3.7")

    // Koin for Unit tests
    testImplementation(Dependencies.koinTest)
    testImplementation(Dependencies.ktorServerTest)
    testImplementation(Dependencies.assertJ)
    testImplementation(Dependencies.junit)
    testRuntimeOnly(Dependencies.junitEngine)
    testImplementation(Dependencies.mockK)
    testImplementation(Dependencies.ktorClientCore)
    testImplementation(Dependencies.ktorClientContentNegotiation)
    testImplementation(Dependencies.ktorClientJetty)
}

tasks {
    test {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }

    // config JVM target to 1.8 for kotlin compilation tasks
    compileKotlin {
        kotlinOptions.jvmTarget = "17"
    }

    shadowJar {
        version = "0.9"
    }
}
