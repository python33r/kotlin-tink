package org.efford.tink

import com.google.crypto.tink.InsecureSecretKeyAccess
import com.google.crypto.tink.Mac
import com.google.crypto.tink.TinkJsonProtoKeysetFormat
import com.google.crypto.tink.mac.MacConfig
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    if (args.size != 3) {
        println("""
            Error: 3 command line arguments are required!
              - filename of MAC key
              - name of file for which an authentication tag is required
              - name of file that will contain the authentication tag
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

    // Read data to be tagged from file specified on command line

    val data = Files.readAllBytes(Paths.get(args[1]))

    // Compute tag for the data

    val primitive = key.getPrimitive(Mac::class.java)
    val tag = primitive.computeMac(data)

    // Write tag to file specified on command line

    Files.write(Paths.get(args[2]), tag)
}
