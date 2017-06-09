WebTest
=======

WebTest isn't being actively worked on at present but is still useful in some scenarios.
It provides excellent reporting and the ability to use Groovy or XML as the testing language
can sometimes be useful.

To invoke the XML ant file, ensure that Apache Ant is on your PATH.
Change to the `src/test/xml` directory in your shell and then execute:

```
./ant -f TestSimpBlogWebTest.xml (the ./ at the start isn't needed for Windows)
```

To invoke the Groovy variant, execute the `TestSimpBlogWebTestScript.groovy` script
in the `src/test/groovy` folder within your IDE or using Gradle from the commandline
using:

```
./gradlew :WebTest:run
```
