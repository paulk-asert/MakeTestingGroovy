Concordion
==========

Concordion is an acceptance testing framework which supports
various formats, such as HTML (used here) and markdown,
for its input specifications.

To use gradle, the `test` task can be used:

```
./gradlew :Concordion:test
```

Have a look in the `build/concordion-results` directory.
You can also run the test within your IDE but the resulting
HTML file will be within a temporary directory.

Further info:
* http://concordion.org/
