package org.efford.tink

import com.google.crypto.tink.InsecureSecretKeyAccess
import com.google.crypto.tink.KeyTemplates
import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.TinkJsonProtoKeysetFormat
import com.google.crypto.tink.hybrid.HybridConfig
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    if (args.size != 2) {
        println("""
            Error: two command line arguments required!
              - path to file that will contain the private key
              - path to file that will contain the public key
        """.trimIndent())
        exitProcess(1)
    }

    val privateKeyPath = Paths.get(args[0])
    val publicKeyPath = Paths.get(args[1])

    // Configure Tink to use hybrid encryption primitives

    HybridConfig.register()

    // Generate key material suitable for ECIES with AEAD and HKDF

    val privateKey = KeysetHandle.generateNew(
        KeyTemplates.get("ECIES_P256_HKDF_HMAC_SHA256_AES128_GCM")
    )

    // Write private key to cleartext JSON file
    // (Note: insecure, done here purely for convenience)

    var serializedKey = TinkJsonProtoKeysetFormat.serializeKeyset(
        privateKey, InsecureSecretKeyAccess.get())

    Files.write(privateKeyPath, serializedKey.toByteArray())

    // Extract public key and write it to another JSON file

    val publicKey = privateKey.publicKeysetHandle
    serializedKey = TinkJsonProtoKeysetFormat.serializeKeysetWithoutSecret(publicKey)

    Files.write(publicKeyPath, serializedKey.toByteArray())
}
