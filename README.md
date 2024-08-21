# kotlin-tink

Examples of using the [Tink][tnk] cryptography library in Kotlin.

Each example is structured as a separate [Gradle][gra] subproject. If you
are working from the command line rather an IDE, the Gradle commands
shown below should all be executed in the top-level directory, not the
subproject directory.

**NOTE**: the cryptographic keys generated and used in these examples
are stored in cleartext on the local disk. This is clearly insecure and
is done here purely to keep the examples simple.

## hmac

This project demonstrates the use of [HMAC][hmc] for message authentication.

To generate an HMAC key, do

```shell
./gradlew :hmac:createHmacKey
```

The key will be stored in the `hmac/build` directory, as file `key.json`.

To use the generated key to compute an authentication tag for the message
`data/message.txt`, do

```shell
./gradlew :hmac:computeTag
```

The tag will be stored in `hmac/build`, as the binary file `message.txt.tag`.

To verify `data/message.txt` using the previously generated key and auth
tag, do

```shell
./gradlew :hmac:verifyTag
```

This should print a message indicating that authentication succeeded.
You can trigger an authentication failure by altering `data/message.txt`
prior to executing this task.

There is a convenient shortcut for running all three tasks in sequence:

```shell
,/gradlew :hmac:hmacdemo
```

## symcipher

This project demonstrates symmetric encryption using [AES][aes] (more
precisely, AES in [Galois/Counter Mode][gcm]).

To generate an AES-GCM key, do

```shell
./gradlew :symcipher:createCipherKey
```

The key will be stored in the `symcipher/build` directory, as file `key.json`.

To use the generated key to encrypt the message `data/message.txt`, do

```shell
./gradlew :symcipher:symEncrypt
```

The resulting ciphertext will be stored in `symcipher/build`, as the binary
file `encrypted.bin`.

To decrypt this ciphertext using the previously generated key, do

```shell
./gradlew :symcipher:symDecrypt
```

The result of decryption will be stored in `symcipher/build`, as the file
`decrypted.txt`. You can compare this with `data/message.txt`; the two
files should be identical.

There is a convenient shortcut for running all three tasks in sequence:

```shell
,/gradlew :symcipher:symdemo
```

## hybrid

This project demonstrates a [hybrid encryption][hyb] scheme, in which a
message is encrypted using AES-GCM and the AES key is protected using
public key encryption.

To generate a private key and public key, do

```shell
./gradlew :hybrid:createKeyPair
```

The keys will be created in `hybrid/build`, as files `private_key.json` and
`public_key.json`.

To encrypt the message in `data/message.txt` using the public key, do

```shell
./gradlew :hybrid:hybridEncrypt
```

The resulting ciphertext will be stored in `hybrid/build`, as the binary
file `encrypted.bin`.

To decrypt this ciphertext using the previously generated private key, do

```shell
./gradlew :hybrid:hybridDecrypt
```

The result of decryption will be stored in `hybrid/build`, as the file
`decrypted.txt`. You can compare this with `data/message.txt`; the two files
should be identical.

There is a convenient shortcut for running all three tasks in sequence:

```shell
,/gradlew :hybrid:hybriddemo
```

[tnk]: https://developers.google.com/tink
[gra]: https://gradle.org/
[hmc]: https://en.wikipedia.org/wiki/HMAC
[aes]: https://en.wikipedia.org/wiki/Advanced_Encryption_Standard
[gcm]: https://en.wikipedia.org/wiki/Galois/Counter_Mode
[hyb]: https://en.wikipedia.org/wiki/Hybrid_cryptosystem
