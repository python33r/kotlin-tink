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
val encrypted = "build/encrypted.bin"
val decrypted = "build/decrypted.txt"

tasks.register<JavaExec>("createHybridKeys") {
    description = "Creates a key pair for hybrid encryption."
    mainClass = "org.efford.tink.CreateHybridKeysKt"
    args = listOf(privateKey, publicKey)
    doLast {
        println("\nKeys written to $privateKey and $publicKey")
    }
}

tasks.register<JavaExec>("hybridEncrypt") {
    description = "Performs hybrid encryption on a sample data file."
    mainClass = "org.efford.tink.HybridEncryptKt"
    args = listOf(publicKey, dataFile, encrypted)
    mustRunAfter("createHybridKeys")
    doLast {
        println("\nCiphertext written to $encrypted")
    }
}

tasks.register<JavaExec>("hybridDecrypt") {
    description = "Decrypts the ciphertext generated by hybrid encryption."
    mainClass = "org.efford.tink.HybridDecryptKt"
    args = listOf(privateKey, encrypted, decrypted)
    mustRunAfter("hybridEncrypt")
    doLast {
        println("\nDecrypted plaintext written to $decrypted")
        println("(Compare this with $dataFile)")
    }
}

tasks.withType(JavaExec::class).configureEach {
    group = "application"
    classpath = sourceSets.main.get().runtimeClasspath
}

tasks.register("hybridDemo") {
    group = "application"
    description = "Runs createHybridKeys, hybridEncrypt & hybridDecrypt tasks."
    dependsOn("createHybridKeys", "hybridEncrypt", "hybridDecrypt")
    doLast {
        println("\nDone!")
    }
}
