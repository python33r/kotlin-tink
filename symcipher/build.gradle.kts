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
        val usedArgs = args as List<String>
        println("\nKey written to ${usedArgs[0]}")
    }
}

tasks.register<JavaExec>("symEncrypt") {
    description = "Encrypts a sample data file."
    mainClass = "org.efford.tink.SymEncryptKt"
    args = listOf(keyFile, dataFile, encrypted)
    mustRunAfter("createCipherKey")
    doLast {
        val usedArgs = args as List<String>
        println("\nPlaintext was ${usedArgs[1]}")
        println("Ciphertext written to ${usedArgs[2]}")
    }
}

tasks.register<JavaExec>("symDecrypt") {
    description = "Decrypts a previously-encrypted file."
    mainClass = "org.efford.tink.SymDecryptKt"
    args = listOf(keyFile, encrypted, decrypted)
    mustRunAfter("symEncrypt")
    doLast {
        val usedArgs = args as List<String>
        println("\nDecrypted plaintext written to ${usedArgs[2]}")
        println("(Compare this with the original plaintext)")
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
    doLast {
        println("\nDone!")
    }
}
