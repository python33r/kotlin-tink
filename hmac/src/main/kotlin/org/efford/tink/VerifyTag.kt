package org.efford.tink

import com.google.crypto.tink.InsecureSecretKeyAccess
import com.google.crypto.tink.Mac
import com.google.crypto.tink.TinkJsonProtoKeysetFormat
import com.google.crypto.tink.mac.MacConfig
import java.io.File
import java.security.GeneralSecurityException
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    if (args.size != 3) {
        println("""
            Error: three command line arguments are required!
              - path to MAC key file
              - path to file for which the authentication tag was computed
              - path to file containing the authentication tag
        """.trimIndent())
        exitProcess(1)
    }

    val keyPath = File(args[0])
    val filePath = File(args[1])
    val tagPath = File(args[2])

    // Configure Tink to use MAC primitives

    MacConfig.register()

    // Load key details from file specified on command line
    // (Note: done insecurely, for convenience)

    val key = TinkJsonProtoKeysetFormat.parseKeyset(
        keyPath.readText(),
        InsecureSecretKeyAccess.get()
    )

    // Read data & tag from files specified on command line

    val data = filePath.readBytes()
    val tag = tagPath.readBytes()

    // Verify tag

    try {
        val primitive = key.getPrimitive(Mac::class.java)
        primitive.verifyMac(tag, data)
        println("Auth tag is valid")
    }
    catch (error: GeneralSecurityException) {
        println(">>> Auth tag is NOT valid! <<<")
    }
}
