import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.5.4"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.5.21"
    kotlin("kapt") version "1.5.21"
    kotlin("plugin.spring") version "1.5.21"
    kotlin("plugin.jpa") version "1.5.21"
}

group = "at.adverity"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11
val queryDslVersion = "5.0.+"

repositories {
    mavenCentral()
}

dependencies {
    kapt("com.querydsl:querydsl-apt:${queryDslVersion}:jpa")
    annotationProcessor("org.springframework.boot:spring-boot-starter-data-jpa:2.1.1.RELEASE")

    compileOnly("org.projectlombok:lombok:1.18.4")
    implementation("com.querydsl:querydsl-core:${queryDslVersion}")
    implementation("com.querydsl:querydsl-jpa:${queryDslVersion}")
    implementation("com.querydsl:querydsl-apt:${queryDslVersion}:jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.github.doyaaaaaken:kotlin-csv-jvm:1.1.+")
    implementation("io.github.microutils:kotlin-logging-jvm:2.0.+")
    runtimeOnly("com.h2database:h2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf(
            "-Xjsr305=strict",
            "-Xjvm-default=enable"
        )
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
