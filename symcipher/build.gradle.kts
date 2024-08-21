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
val encrypted = "build/encrypted.bin"
val decrypted = "build/decrypted.txt"

tasks.register<JavaExec>("createCipherKey") {
    description = "Creates a key for AES-GCM encryption."
    mainClass = "org.efford.tink.CreateCipherKeyKt"
    args = listOf(keyFile)
    doLast {
        println("\nKey written to $keyFile")
    }
}

tasks.register<JavaExec>("symEncrypt") {
    description = "Encrypts a sample data file."
    mainClass = "org.efford.tink.SymEncryptKt"
    args = listOf(keyFile, dataFile, encrypted)
    mustRunAfter("createCipherKey")
    doLast {
        println("\nCiphertext written to $encrypted")
    }
}

tasks.register<JavaExec>("symDecrypt") {
    description = "Decrypts a previously-encrypted file."
    mainClass = "org.efford.tink.SymDecryptKt"
    args = listOf(keyFile, encrypted, decrypted)
    mustRunAfter("symEncrypt")
    doLast {
        println("\nDecrypted plaintext written to $decrypted")
        println("(Compare this with $dataFile)")
    }
}

tasks.withType(JavaExec::class).configureEach {
    group = "application"
    classpath = sourceSets.main.get().runtimeClasspath
}

tasks.register("symDemo") {
    group = "application"
    description = "Runs createCipherKey, symEncrypt & symDecrypt tasks."
    dependsOn("createCipherKey", "symEncrypt", "symDecrypt")
}
