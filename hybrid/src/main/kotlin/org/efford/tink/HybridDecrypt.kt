package org.efford.tink

import com.google.crypto.tink.HybridDecrypt
import com.google.crypto.tink.InsecureSecretKeyAccess
import com.google.crypto.tink.TinkJsonProtoKeysetFormat
import com.google.crypto.tink.hybrid.HybridConfig
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    if (args.size != 3) {
        println("""
            Error: three command line arguments are required!
              - path to file containing the public key
              - path to ciphertext that is to be decrypted
              - path to file that will contain the resulting plaintext
        """.trimIndent())
        exitProcess(1)
    }

    val privateKeyPath = Paths.get(args[0])
    val ciphertextPath = Paths.get(args[1])
    val plaintextPath = Paths.get(args[2])

    // Configure Tink to use hybrid encryption primitives

    HybridConfig.register()

    // Load key material from JSON file specified on the command line
    // (Note: insecure, done this way purely for convenience)

    val key = TinkJsonProtoKeysetFormat.parseKeyset(
        String(Files.readAllBytes(privateKeyPath)),
        InsecureSecretKeyAccess.get()
    )

    // Read ciphertext from file specified on the command line

    val ciphertext = Files.readAllBytes(ciphertextPath)

    // Perform the decryption

    val primitive = key.getPrimitive(HybridDecrypt::class.java)
    val plaintext = primitive.decrypt(ciphertext, null)

    // Write plaintext to file specified on the command line

    Files.write(plaintextPath, plaintext)
}
