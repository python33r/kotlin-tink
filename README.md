# kotlin-tink

Examples of using the [Tink][tnk] cryptography library in Kotlin.

Each example is structured as a separate [Gradle][gra] subproject. If you
are working from the command line rather an IDE, the Gradle tasks shown
should all be executed in the top-level directory, not the subproject
directory.

The Gradle tasks below use defaults for all of the required files. If you
wish to specify your own files, you can add the `--args` option on the
command line when executing a task, following it with a quoted string
containing all of the command line arguments that the corresponding
program expects to receive.

**NOTE**: the cryptographic keys generated and used in these examples
are stored in cleartext on the local disk. This is clearly insecure and
is done here purely to keep the examples simple.

## hmac

This project demonstrates the use of [HMAC][hmc] for message authentication.

To generate an HMAC key, do

```shell
./gradlew createHmacKey
```

The key will be stored in the `hmac/build` directory, as file `key.json`.

To use the generated key to compute an authentication tag for the message
`data/message.txt`, do

```shell
./gradlew computeTag
```

The tag will be stored in `hmac/build`, as the binary file `message.txt.tag`.

To verify `data/message.txt` using the previously generated key and auth
tag, do

```shell
./gradlew verifyTag
```

This should print a message indicating that authentication succeeded.
You can trigger an authentication failure by altering `data/message.txt`
prior to executing this task.

There is a convenient shortcut for running all three tasks in sequence:

```shell
,/gradlew hmacDemo
```

## symcipher

This project demonstrates symmetric encryption using [AES][aes] (more
precisely, AES in [Galois/Counter Mode][gcm]).

To generate an AES-GCM key, do

```shell
./gradlew createCipherKey
```

The key will be stored in the `symcipher/build` directory, as file `key.json`.

To use the generated key to encrypt the message `data/message.txt`, do

```shell
./gradlew symEncrypt
```

The resulting ciphertext will be stored in `symcipher/build`, as the binary
file `encrypted.bin`.

To decrypt this ciphertext using the previously generated key, do

```shell
./gradlew symDecrypt
```

The result of decryption will be stored in `symcipher/build`, as the file
`decrypted.txt`. You can compare this with `data/message.txt`; the two
files should be identical.

There is a convenient shortcut for running all three tasks in sequence:

```shell
,/gradlew symDemo
```

## hybrid

This project demonstrates a [hybrid encryption][hyb] scheme, in which a
message is encrypted using AES-GCM and the AES key is protected using
public key encryption.

To generate a private key and public key, do

```shell
./gradlew createHybridKeys
```

The keys will be created in `hybrid/build`, as files `private_key.json` and
`public_key.json`.

To encrypt the message in `data/message.txt` using the public key, do

```shell
./gradlew hybridEncrypt
```

The resulting ciphertext will be stored in `hybrid/build`, as the binary
file `encrypted.bin`.

To decrypt this ciphertext using the previously generated private key, do

```shell
./gradlew hybridDecrypt
```

The result of decryption will be stored in `hybrid/build`, as the file
`decrypted.txt`. You can compare this with `data/message.txt`; the two files
should be identical.

There is a convenient shortcut for running all three tasks in sequence:

```shell
,/gradlew hybridDemo
```

## signature

This project demonstrates the use of Tink to generate and verify Ed25519
digital signatures.

To create the key pair needed for signing and verification, do

```shell
./gradlew createSigKeys
```

The keys will be created in `signature/build`, as files `private_key.json`
and `public_key.json`.

To sign the file `data/message.txt` with the previously generated private
key, do

```shell
./gradlew signFile
```

The signature will be created in `signature/build`, as the file
`message.txt.sig`.

To verify the signature generated for `data/message.txt`, do

```shell
./gradlew verifySig
```

This should print a message indicating that the signature is OK. You can
make the verification fail by altering `data/message.txt` prior to executing
this task.

There is a convenient shortcut for running all three tasks in sequence:

```shell
./gradlew sigDemo
```

[tnk]: https://developers.google.com/tink
[gra]: https://gradle.org/
[hmc]: https://en.wikipedia.org/wiki/HMAC
[aes]: https://en.wikipedia.org/wiki/Advanced_Encryption_Standard
[gcm]: https://en.wikipedia.org/wiki/Galois/Counter_Mode
[hyb]: https://en.wikipedia.org/wiki/Hybrid_cryptosystem
