package org.efford.tink

import com.google.crypto.tink.InsecureSecretKeyAccess
import com.google.crypto.tink.Mac
import com.google.crypto.tink.TinkJsonProtoKeysetFormat
import com.google.crypto.tink.mac.MacConfig
import java.nio.file.Files
import java.nio.file.Paths
import java.security.GeneralSecurityException
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    if (args.size != 3) {
        println("""
            Error: 3 command line arguments are required!
              - filename of MAC key
              - name of file to be verified using the key
              - name of file containing the authentication tag
        """.trimIndent())
        exitProcess(1)
    }

    // Configure Tink to use MAC primitives

    MacConfig.register()

    // Load key details from file specified on command line
    // (Note: done insecurely, for convenience)

    val serializedKey = String(Files.readAllBytes(Paths.get(args[0])))

    val key = TinkJsonProtoKeysetFormat.parseKeyset(
        serializedKey,
        InsecureSecretKeyAccess.get()
    )

    // Read data & tag from files specified on command line

    val data = Files.readAllBytes(Paths.get(args[1]))
    val tag = Files.readAllBytes(Paths.get(args[2]))

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
