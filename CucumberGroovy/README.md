Cucumber with Groovy
====================

This project illustrates how to use Cucumber's Groovy
language support to test the blogging application.

The feature file is in `src/test/resources/NewPost.feature`.

The glue steps are defined in `src/test/groovy/Steps.groovy`.

The predefined `run` task can be used to invoke the test:

```
./gradlew :CucumberGroovy:run
```

This invokes the cucumber CLI with switches to tell it where
to find the feature and glue files.

Further info:
* https://cucumber.io/
