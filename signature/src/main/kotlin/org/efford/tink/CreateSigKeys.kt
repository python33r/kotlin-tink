package org.efford.tink

import com.google.crypto.tink.InsecureSecretKeyAccess
import com.google.crypto.tink.KeyTemplates
import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.TinkJsonProtoKeysetFormat
import com.google.crypto.tink.signature.SignatureConfig
import java.io.File
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

    val privateKeyPath = File(args[0])
    val publicKeyPath = File(args[1])

    // Configure Tink to use digital signature primitives

    SignatureConfig.register()

    // Generate key material suitable for Ed25519-based signatures

    val privateKey = KeysetHandle.generateNew(KeyTemplates.get("ED25519"))

    // Write private key to cleartext JSON file
    // (Note: insecure, done here purely for convenience)

    var serializedKey = TinkJsonProtoKeysetFormat.serializeKeyset(
        privateKey, InsecureSecretKeyAccess.get())

    privateKeyPath.writeText(serializedKey)

    // Extract public key and write it to another JSON file

    val publicKey = privateKey.publicKeysetHandle
    serializedKey = TinkJsonProtoKeysetFormat.serializeKeysetWithoutSecret(publicKey)

    publicKeyPath.writeText(serializedKey)
}
