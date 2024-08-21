package org.efford.tink

import com.google.crypto.tink.InsecureSecretKeyAccess
import com.google.crypto.tink.PublicKeySign
import com.google.crypto.tink.TinkJsonProtoKeysetFormat
import com.google.crypto.tink.signature.SignatureConfig
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    if (args.size != 3) {
        println("""
            Error: 3 command line arguments are required!
              - path to private key file
              - path to file that is to be signed
              - path to file that will contain the signature
        """.trimIndent())
        exitProcess(1)
    }

    val keyPath = Paths.get(args[0])
    val filePath = Paths.get(args[1])
    val signaturePath = Paths.get(args[2])

    // Configure Tink to use digital signature primitives

    SignatureConfig.register()

    // Load private key from file specified on command line
    // (Note: insecure, done this way purely for convenience)

    val key = TinkJsonProtoKeysetFormat.parseKeyset(
        String(Files.readAllBytes(keyPath)),
        InsecureSecretKeyAccess.get()
    )

    // Read contents of file to be signed

    val data = Files.readAllBytes(filePath)

    // Sign file contents

    val signer = key.getPrimitive(PublicKeySign::class.java)
    val signature = signer.sign(data)

    // Write signature to a binary file

    Files.write(signaturePath, signature)
}
