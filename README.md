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
./gradlew :hmac:createKey
```

The key will be stored in the `hmac/build` directory, as file `key.json`.

To use the generated key to compute an authentication tag for the message
`data/message.txt`, do

```shell
./gradlew :hmac:computeTag
```

The tag will be stored in `hmac/build`, as the file `message.txt.tag`.

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
,/gradlew :hmac:all
```

[tnk]: https://developers.google.com/tink
[gra]: https://gradle.org/
[hmc]: https://en.wikipedia.org/wiki/HMAC
