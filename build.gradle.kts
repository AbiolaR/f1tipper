import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.6.3"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.6.10"
	kotlin("plugin.spring") version "1.6.10"
	kotlin("plugin.jpa") version "1.6.10"
	id("com.google.cloud.tools.jib") version "3.2.0"
}

group = "com.keplerworks"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("com.auth0:java-jwt:3.18.3")
	implementation("com.squareup.retrofit2:retrofit:2.9.0")
	implementation("com.squareup.retrofit2:converter-gson:2.9.0")
	implementation("com.google.code.gson:gson:2.9.0")
	runtimeOnly("mysql:mysql-connector-java")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	implementation("io.github.microutils:kotlin-logging-jvm:2.1.21")
	implementation("com.github.ulisesbocchio:jasypt-spring-boot-starter:3.0.4")
	implementation("org.bouncycastle:bcprov-jdk15on:1.70")
	implementation("nl.martijndwars:web-push:5.1.1")
	implementation("com.github.shyiko.skedule:skedule:0.4.0")
}

tasks.register("removeStaticFolder") {
	val staticDir = layout.projectDirectory.dir("src/main/resources/static")
	doFirst {
		delete(staticDir)
	}
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

jib.from.image = "openjdk:17.0.2"
jib.to.image = "kworks/f1tipper"
jib.to.credHelper = "wincred"
jib.container.ports = listOf("80", "443", "3306")