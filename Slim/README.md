Slim/FitNesse
=============

This directory illustrates numerous approaches to using Slim/FitNesse including writing the Fixture files using Groovy.

Running FitNesse
----------------

Fitnesse can be run manually as follows (shown running on port 9090 which can be changed if needed):

    > cd fitnesse
    > java -jar finesse.jar -p 9090

There is a shortcut to run fitnesse via gradle (but standard output is hidden if doing this):

    > ..\gradlew start

You can later stop it with:

    > ..\gradlew stop

Traverse to the following page to browse the available test suites and peruse the FitNesse user guide:

    http://localhost:9090/FitNesse

Groovy Fixtures
---------------

The gradle build file will compile the fixture files and assemble the necessary libraries by using the
following command (shown for windows - switch the slash around for most other operating systems):

    > ..\gradlew dist

To run the tests traverse to the following page and then click 'Test':

    http://localhost:9090/FitNesse.SimpBlogGroovyFixtureSuite.SimpBlogTest

Or from the Suite page click 'Suite':

    http://localhost:9090/FitNesse.SimpBlogGroovyFixtureSuite

Alternatively, you can run the following JUnit test with Groovy/in an IDE:

    FitNesseSuiteJUnitRunnerTest

HtmlFixture Example
-------------------

By way of comparison, there is a more traditional HtmlFixture based Suite here:

    http://localhost:9090/FitNesse.SimpBlogHtmlFixtureSuite

A typical non-refactored test is here:

    http://localhost:9090/FitNesse.SimpBlogHtmlFixtureSuite.SimpBlogTest

A further test illustrating some validation checking for the blog title is here:

    http://localhost:9090/FitNesse.SimpBlogHtmlFixtureSuite.TitleValidationTest

Before using this suite you will need to download the HtmlFixture libraries using the following command:

    > ..\gradlew downloadHtmlFixture

Selenium with Xebium and ZiBreve
--------------------------------

The final set of examples use [Xebium](http://xebia.github.com/Xebium/) and [ZiBreve](http://tech.groups.yahoo.com/group/fitnesse/message/17812).
Xebium has both a Selenium-based FitNesse fixture and associated Selenium IDE plugin for [Firefox](http://www.mozilla.org/firefox).
You need to download the Selenium IDE plugin and then the Xebium formatter plugin if you wish to use the recorder functionality.
We discourage you from using recorder tools such as Selenium IDE without appropriate regard for test maintainability and refactoring.

There are four examples using this approach which illustrate progressive refactoring of the tests:
* __SimpBlogXebiumSuiteV1__ illustrates some traditional unrefactored tests
* __SimpBlogXebiumSuiteV2__ illustrates manually using some variable definitions to make different tests appear similar
* __SimpBlogXebiumSuiteV3__ illustrates using ZiBreve to identify common abstractions (we manually converted the resulting *DefinedAction* into a Slim scenario)
* __SimpBlogXebiumSuiteV4__ illustrates using nested ScenarioLibraries to achieve BDD style Slim tests

To perform the ZiBreve refactoring yourself you should download it from the ZiBreve [download page](https://sourceforge.net/projects/fitlibrary/files/ZiBreve/).
Create a new project giving it a name, e.g. `SimpBlogWiki` and point it to the embedded FitNesse pages (`Slim/fitnesse/FitNesseRoot/FitNesse`).
Use `Find Abstractions`, `Create Defined Action` and `Find Potential Calls` to refactor your tests. The results will be refactored tests using
*DefinedActions*. These aren't quite compatible with the Slim runner we are using. The ZiBreve User Guide gives further details
about converting between the two.

Other details
-------------

If you are interested in this style of testing for Grails, see here:
http://bodiam.github.com/grails-fitnesse/fitnesse/docs/manual/index.html
