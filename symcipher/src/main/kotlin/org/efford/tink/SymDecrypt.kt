package org.efford.tink

import com.google.crypto.tink.Aead
import com.google.crypto.tink.InsecureSecretKeyAccess
import com.google.crypto.tink.TinkJsonProtoKeysetFormat
import com.google.crypto.tink.aead.AeadConfig
import java.io.File
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    if (args.size != 3) {
        println("""
            Error: three command line arguments are required!
              - path to previously-generated cipher key
              - path to file that is to be decrypted
              - path to file that will contain the resulting plaintext
        """.trimIndent())
        exitProcess(1)
    }

    val keyPath = File(args[0])
    val ciphertextPath = File(args[1])
    val plaintextPath = File(args[2])

    // Configure Tink to do AEAD

    AeadConfig.register()

    // Load key material from JSON file specified on the command line
    // (Note: done insecurely, for convenience)

    val key = TinkJsonProtoKeysetFormat.parseKeyset(
        keyPath.readText(), InsecureSecretKeyAccess.get())

    // Read ciphertext from file specified on the command line

    val ciphertext = ciphertextPath.readBytes()

    // Perform the decryption

    val primitive = key.getPrimitive(Aead::class.java)
    val plaintext = primitive.decrypt(ciphertext, null)

    // Write plaintext to file specified on the command line

    plaintextPath.writeBytes(plaintext)
}
