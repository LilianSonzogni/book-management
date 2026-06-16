plugins {
    kotlin("jvm") version "2.2.21"
    kotlin("plugin.spring") version "2.2.21"
    id("org.springframework.boot") version "4.0.6"
    id("io.spring.dependency-management") version "1.1.7"
    jacoco
    id("info.solidsoft.pitest") version "1.19.0"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
description = "TestUnitaireTp1"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-liquibase")
    implementation("org.postgresql:postgresql")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("io.kotest:kotest-runner-junit5:5.9.1")
    testImplementation("io.kotest:kotest-assertions-core:5.9.1")
    testImplementation("io.kotest:kotest-property:5.9.1")
    testImplementation("io.mockk:mockk:1.13.13")
}

testing {
    suites {
        val testIntegration by registering(JvmTestSuite::class) {
            sources {
                kotlin {
                    setSrcDirs(listOf("src/testIntegration/kotlin"))
                }
                compileClasspath += sourceSets.main.get().output
                runtimeClasspath += sourceSets.main.get().output
            }
        }
        val testComponent by registering(JvmTestSuite::class) {
            sources {
                kotlin {
                    setSrcDirs(listOf("src/testComponent/kotlin"))
                }
                resources {
                    setSrcDirs(listOf("src/testComponent/resources"))
                }
                compileClasspath += sourceSets.main.get().output
                runtimeClasspath += sourceSets.main.get().output
            }
        }
    }
}

val testIntegrationImplementation: Configuration by configurations.getting {
    extendsFrom(configurations.implementation.get())
}

val testComponentImplementation: Configuration by configurations.getting {
    extendsFrom(configurations.implementation.get())
}

dependencies {
    testIntegrationImplementation("io.mockk:mockk:1.13.13")
    testIntegrationImplementation("io.kotest:kotest-assertions-core:5.9.1")
    testIntegrationImplementation("io.kotest:kotest-runner-junit5:5.9.1")
    testIntegrationImplementation("com.ninja-squad:springmockk:4.0.2")
    testIntegrationImplementation("io.kotest.extensions:kotest-extensions-spring:1.3.0")
    testIntegrationImplementation("org.springframework.boot:spring-boot-starter-webmvc-test") {
        exclude(module = "mockito-core")
    }
    testIntegrationImplementation("org.testcontainers:testcontainers:1.21.4")
    testIntegrationImplementation("org.testcontainers:jdbc:1.21.4")
    testIntegrationImplementation("org.testcontainers:postgresql:1.21.4")
    testIntegrationImplementation("io.kotest.extensions:kotest-extensions-testcontainers:2.0.2")

    testComponentImplementation("io.cucumber:cucumber-java:7.34.3")
    testComponentImplementation("io.cucumber:cucumber-spring:7.34.3")
    testComponentImplementation("io.cucumber:cucumber-junit-platform-engine:7.34.3")
    testComponentImplementation("io.rest-assured:rest-assured:6.0.0")
    testComponentImplementation("org.junit.platform:junit-platform-suite:6.0.3")
    testComponentImplementation("org.testcontainers:testcontainers:1.21.4")
    testComponentImplementation("org.testcontainers:jdbc:1.21.4")
    testComponentImplementation("org.testcontainers:postgresql:1.21.4")
    testComponentImplementation("io.kotest:kotest-assertions-core:5.9.1")
    testComponentImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(module = "mockito-core")
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
    }
}

configurations.all {
    resolutionStrategy.eachDependency {
        if (requested.group == "org.junit.platform" && requested.name == "junit-platform-launcher") {
            useVersion("6.0.3")
            because("align junit-platform-launcher with junit-platform-engine 6.0.3")
        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test, tasks.named("testIntegration"), tasks.named("testComponent"))
    executionData(
        tasks.test.get(),
        tasks.named<Test>("testIntegration").get(),
        tasks.named<Test>("testComponent").get(),
    )
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}

pitest {
    junit5PluginVersion.set("1.2.2")
    pitestVersion.set("1.19.1")
    targetClasses.set(setOf("com.example.testunitairetp1.domain.*"))
    targetTests.set(setOf("com.example.testunitairetp1.domain.*"))
    outputFormats.set(setOf("HTML", "XML"))
}

