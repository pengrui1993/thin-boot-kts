plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.4.1"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(20)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

val depLibsDir = "$projectDir/build/libs/lib"
tasks.register<Copy>("copyDependencies") {
	from(configurations.runtimeClasspath).into(depLibsDir)
}
tasks.withType<Jar>{
	manifest {
		val cps = configurations.runtimeClasspath.get()
                    .files.joinToString(" ") { "lib/${it.name}" }
		attributes["Class-Path"] = cps
		attributes["Main-Class"] = "com.example.demo.DemoApplicationKt"
	}
}
tasks {
    // Task to copy dependencies to "target/lib"
    val copyDependencies0 by registering(Copy::class) {
        from(configurations.runtimeClasspath)
        into(depLibsDir)
    }
    // Ensure dependencies are copied before building the JAR
    jar {
        dependsOn(copyDependencies0)
    }
}

tasks.build {
    dependsOn(tasks.jar)
}
