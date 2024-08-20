plugins {
    kotlin("jvm")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.crypto.tink:tink:1.14.1")
}

kotlin {
    jvmToolchain(21)
}

val keyFile = "build/key.json"
val dataFile = "../data/message.txt"
val authTag = "build/message.txt.tag"

tasks.register<JavaExec>("createKey") {
    description = "Creates an HMAC key."
    mainClass = "org.efford.tink.CreateKeyKt"
    args = listOf(keyFile)
    doLast {
        println("\nKey written to $keyFile")
    }
}

tasks.register<JavaExec>("computeTag") {
    description = "Computes an authentication tag for a sample data file."
    mainClass = "org.efford.tink.ComputeTagKt"
    args = listOf(keyFile, dataFile, authTag)
    mustRunAfter("createKey")
    doLast {
        println("\nAuth tag written to $authTag")
    }
}

tasks.register<JavaExec>("verifyTag") {
    description = "Verifies the authentication tag computed for a file."
    mainClass = "org.efford.tink.VerifyTagKt"
    args = listOf(keyFile, dataFile, authTag)
    mustRunAfter("computeTag")
    doFirst {
        println()
    }
}

tasks.withType(JavaExec::class).configureEach {
    group = "application"
    classpath = sourceSets.main.get().runtimeClasspath
}

tasks.register("all") {
    group = "application"
    description = "Runs createKey, computeTag & verifyTag tasks."
    dependsOn("createKey", "computeTag", "verifyTag")
}
