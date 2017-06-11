Cucumber with Java
==================

This project illustrates how to use Cucumber's Java/JUnit
language support to test the blogging application but
we'll use Groovy instead of Java.

The feature file is in `src/test/resources/InvalidPost.feature`.

The glue steps are defined in `src/test/groovy/TestSimpBlogSteps.groovy`.

The JUnit bootstrap file is in `src/test/groovy/TestSimpBlogCuke.groovy`.
It informs JUnit to use Cucumber's JUnit test runner and tells it where
the feature file(s) are to be found.

The normal Gradle `test` task can be used to invoke the test:

```
./gradlew :CucumberJava:test
```

Using a vanilla JUnit environment might be useful in some scenarios.
A downside is that the default reporting sorts the tests alphabetically,
so the report format is less useful (see `build/reports/tests`).
The supplied Gradle build turns on logging which shows the correct order
of passing tests during execution of the Gradle build.
Also, the html plugin for cucumber is enabled which produces some
better output (see `build/reports/cucumber`)

Further info:
* https://cucumber.io/
