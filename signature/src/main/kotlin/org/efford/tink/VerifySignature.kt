package org.efford.tink

import com.google.crypto.tink.PublicKeyVerify
import com.google.crypto.tink.TinkJsonProtoKeysetFormat
import com.google.crypto.tink.signature.SignatureConfig
import java.io.File
import java.security.GeneralSecurityException
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    if (args.size != 3) {
        println("""
            Error: three command line arguments are required!
              - path to public key file
              - path to file that is to be verified
              - path to signature file
        """.trimIndent())
        exitProcess(1)
    }

    val keyPath = File(args[0])
    val filePath = File(args[1])
    val signaturePath = File(args[2])

    // Configure Tink to use digital signature primitives

    SignatureConfig.register()

    // Load public key from file specified on command line

    val serializedKey = keyPath.readText()
    val key = TinkJsonProtoKeysetFormat.parseKeysetWithoutSecret(serializedKey)

    // Read contents of file and corresponding signature

    val data = filePath.readBytes()
    val signature = signaturePath.readBytes()

    // Attempt to verify signature

    val verifier = key.getPrimitive(PublicKeyVerify::class.java)

    try {
        verifier.verify(signature, data)
        println("Signature is valid")
    }
    catch (error: GeneralSecurityException) {
        println(">>> Signature is NOT valid! <<<")
    }
}
