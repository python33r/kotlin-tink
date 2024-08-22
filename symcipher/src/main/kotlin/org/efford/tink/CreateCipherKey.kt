package org.efford.tink

import com.google.crypto.tink.InsecureSecretKeyAccess
import com.google.crypto.tink.KeyTemplates
import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.TinkJsonProtoKeysetFormat
import com.google.crypto.tink.aead.AeadConfig
import java.io.File
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    if (args.size != 1) {
        println("Error: path to key file required as a command line argument")
        exitProcess(1)
    }

    val keyPath = File(args[0])

    // Configure Tink to use AEAD primitives

    AeadConfig.register()

    // Generate key material suitable for 128-bit AES-GCM

    val key = KeysetHandle.generateNew(KeyTemplates.get("AES128_GCM"))

    // Write key details to specified output file
    // (Note: insecure, done here purely for convenience)

    val serializedKey = TinkJsonProtoKeysetFormat.serializeKeyset(
        key, InsecureSecretKeyAccess.get()
    )

    keyPath.writeText(serializedKey)
}
