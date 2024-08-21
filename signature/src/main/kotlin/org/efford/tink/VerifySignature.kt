package org.efford.tink

import com.google.crypto.tink.PublicKeyVerify
import com.google.crypto.tink.TinkJsonProtoKeysetFormat
import com.google.crypto.tink.signature.SignatureConfig
import java.nio.file.Files
import java.nio.file.Paths
import java.security.GeneralSecurityException
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    if (args.size != 3) {
        println("""
            Error: 3 command line arguments are required!
              - path to public key file
              - path to file that is to be verified
              - path to file containing the signature
        """.trimIndent())
        exitProcess(1)
    }

    val keyPath = Paths.get(args[0])
    val filePath = Paths.get(args[1])
    val signaturePath = Paths.get(args[2])

    // Configure Tink to use digital signature primitives

    SignatureConfig.register()

    // Load public key from file specified on command line

    val serializedKey = String(Files.readAllBytes(keyPath))
    val key = TinkJsonProtoKeysetFormat.parseKeysetWithoutSecret(serializedKey)

    // Read contents of file and corresponding signature

    val data = Files.readAllBytes(filePath)
    val signature = Files.readAllBytes(signaturePath)

    // Attempt to verify signature

    val verifier = key.getPrimitive(PublicKeyVerify::class.java)

    try {
        verifier.verify(signature, data)
        println("Signature is valid.")
    }
    catch (error: GeneralSecurityException) {
        println(">>> Signature is NOT valid! <<<")
    }
}
