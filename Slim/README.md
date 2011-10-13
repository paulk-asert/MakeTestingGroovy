This directory illustrates using Slim/FitNesse with the Fixture files written in Groovy.

The gradle build file will compile the fixture files and assemble the necessary libraries by using the
following command (shown for windows - switch the slash around for most other operating systems):

    > ..\gradlew dist

Fitnesse can be run manually as follows (shown running on port 9090 which can be changed if needed):

    > cd fitnesse
    > java -jar finesse.jar -p 9090

There is a shortcut to run fitnesse via gradle (but standard output is hidden if doing this)
which will automatically do both the above steps:

    > ..\gradlew runFitnesse

To run the tests traverse to the following page and then click 'Test':

    http://localhost:9090/FitNesse.SimpBlogGroovyFixtureSuite.SimpBlogTest

Or from the Suite page click 'Suite':

    http://localhost:9090/FitNesse.SimpBlogGroovyFixtureSuite

You may need to adjust some of the hard-coded directories by editing this page:

    http://localhost:9090/FitNesse.SimpBlogGroovyFixtureSuite

Alternatively, you can run the following JUnit test with Groovy/in an IDE:

    FitNesseSuiteJUnitRunnerTest

By way of comparison, there is a more traditional HtmlFixture based Suite here:

    http://localhost:9090/FitNesse.SimpBlogHtmlFixtureSuite

The non-refactored version is here:

    http://localhost:9090/FitNesse.SimpBlogHtmlFixtureSuite.SimpBlogTest
