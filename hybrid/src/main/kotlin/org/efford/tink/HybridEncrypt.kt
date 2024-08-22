package org.efford.tink

import com.google.crypto.tink.HybridEncrypt
import com.google.crypto.tink.TinkJsonProtoKeysetFormat
import com.google.crypto.tink.hybrid.HybridConfig
import java.io.File
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    if (args.size != 3) {
        println("""
            Error: three command line arguments are required!
              - path to file containing the private key
              - path to file that is to be encrypted
              - path to file that will contain the resulting ciphertext
        """.trimIndent())
        exitProcess(1)
    }

    val publicKeyPath = File(args[0])
    val plaintextPath = File(args[1])
    val ciphertextPath = File(args[2])

    // Configure Tink to use hybrid encryption primitives

    HybridConfig.register()

    // Load key material from JSON file specified on the command line

    val serializedKey = publicKeyPath.readText()
    val key = TinkJsonProtoKeysetFormat.parseKeysetWithoutSecret(serializedKey)

    // Read plaintext from file specified on the command line

    val plaintext = plaintextPath.readBytes()

    // Perform the encryption

    val primitive = key.getPrimitive(HybridEncrypt::class.java)
    val ciphertext = primitive.encrypt(plaintext, null)

    // Write ciphertext to file specified on the command line

    ciphertextPath.writeBytes(ciphertext)
}
