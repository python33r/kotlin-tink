package org.efford.tink

import com.google.crypto.tink.InsecureSecretKeyAccess
import com.google.crypto.tink.PublicKeySign
import com.google.crypto.tink.TinkJsonProtoKeysetFormat
import com.google.crypto.tink.signature.SignatureConfig
import java.io.File
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    if (args.size != 3) {
        println("""
            Error: three command line arguments are required!
              - path to private key file
              - path to file that is to be signed
              - path to signature file
        """.trimIndent())
        exitProcess(1)
    }

    val keyPath = File(args[0])
    val filePath = File(args[1])
    val signaturePath = File(args[2])

    // Configure Tink to use digital signature primitives

    SignatureConfig.register()

    // Load private key from file specified on command line
    // (Note: insecure, done this way purely for convenience)

    val key = TinkJsonProtoKeysetFormat.parseKeyset(
        keyPath.readText(),
        InsecureSecretKeyAccess.get()
    )

    // Read contents of file to be signed

    val data = filePath.readBytes()

    // Sign file contents

    val signer = key.getPrimitive(PublicKeySign::class.java)
    val signature = signer.sign(data)

    // Write signature to a binary file

    signaturePath.writeBytes(signature)
}
