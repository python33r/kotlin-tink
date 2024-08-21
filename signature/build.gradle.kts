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

val privateKey = "build/private_key.json"
val publicKey = "build/public_key.json"
val dataFile = "../data/message.txt"
val signature = "build/message.txt.sig"

tasks.register<JavaExec>("createSigKeys") {
    description = "Creates a key pair for Ed25519 digital signatures."
    mainClass = "org.efford.tink.CreateSigKeysKt"
    args = listOf(privateKey, publicKey)
    doLast {
        println("\nKeys written to $privateKey and $publicKey")
    }
}

tasks.register<JavaExec>("signFile") {
    description = "Signs a sample data file."
    mainClass = "org.efford.tink.SignFileKt"
    args = listOf(privateKey, dataFile, signature)
    mustRunAfter("createSigKeys")
    doLast {
        println("\n$dataFile signed")
        println("Signature written to $signature")
    }
}

tasks.register<JavaExec>("verifySig") {
    description = "Verifies signature on a previously-signed file."
    mainClass = "org.efford.tink.VerifySignatureKt"
    args = listOf(publicKey, dataFile, signature)
    mustRunAfter("signFile")
    doFirst {
        println()
    }
}

tasks.withType(JavaExec::class).configureEach {
    group = "application"
    classpath = sourceSets.main.get().runtimeClasspath
}

tasks.register("sigDemo") {
    group = "application"
    description = "Runs createSigKeys, signFile & verifySig tasks."
    dependsOn("createSigKeys", "signFile", "verifySig")
    doLast {
        println("\nDone!")
    }
}
