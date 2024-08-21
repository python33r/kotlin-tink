package org.efford.tink

import com.google.crypto.tink.InsecureSecretKeyAccess
import com.google.crypto.tink.KeyTemplates
import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.TinkJsonProtoKeysetFormat
import com.google.crypto.tink.mac.MacConfig
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    if (args.size != 1) {
        println("Error: key filename required as a command line argument")
        exitProcess(1)
    }

    val keyPath = Paths.get(args[0])

    // Configure Tink to use MAC primitives

    MacConfig.register()

    // Get key material suitable for use with HMAC-SHA-256

    val key = KeysetHandle.generateNew(KeyTemplates.get("HMAC_SHA256_256BITTAG"))

    // Write key details to specified output file
    // (Note: insecure, done here purely for convenience)

    val serializedKey = TinkJsonProtoKeysetFormat.serializeKeyset(
        key, InsecureSecretKeyAccess.get())

    Files.write(keyPath, serializedKey.encodeToByteArray())
}
