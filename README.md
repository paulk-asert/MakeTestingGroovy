Making Testing Groovy
=====================

This project looks at a simple blog application and numerous tools and techniques to test it. In that sense, the examples
focus on web testing (and to some degree acceptance testing) - having said that, many of the techniques are generally
applicable and can be applied to any kind of testing for any kind of application or system. The examples are mostly
[Groovy](http://groovy-lang.org/) based but again, the ideas transfer across to some degree to other modern languages.
The examples are designed to be runnable 'out-of-the-box' by cutting and pasting the code into the GroovyConsole which
comes with any typical Groovy installation. Alternatively, they can be run from your favorite IDE (tested with IDEA
community edition and, to a lesser degree, Eclipse).

Note: All examples have been tested using Java 8 only unless otherwise noted within the example. Examples may work with
other versions of Java but that probably hasn't been tested.
The examples use the Groovy version specified in `build.gradle` unless otherwise noted.

Application Under Test
----------------------

The application is a simple web-based blogging system. It is a little novel in that it combines in a single
Groovy file a Spring/standalone GORM/Jetty application. Just follow the instructions within the project directory
to fire up the application. The application can be found in the __AppUnderTest__ subproject.
You can also play with a standalone GORM with the same model but no web later (see the __GormApp__ project).

Basic Test Automation
---------------------

* The __Vanilla__ examples illustrate how to test the blogging application using basic low-level Groovy/Java features. No testing framework is used.
* The __HtmlUnit__ examples illustrate how to use [HtmlUnit](http://htmlunit.sourceforge.net/) as the test 'driver' to test the blogging application.
Various test 'runners' are illustrated: plain groovy, [JUnit](http://www.junit.org/) 3/4/5, [TestNG](http://testng.org)
and GroovyTestCase. Various enhancements are examined: a test case abstraction, a fluent test api and a testing DSL.
* The __JWebUnit__ examples illustrate how to use [JWebUnit](http://jwebunit.sourceforge.net/) as the test 'driver' to test the blogging application.
which supports IE, Firefox/Mozilla and Safari.
* The __Geb__ examples illustrate how to use [Geb](http://www.gebish.org/) to test the blogging application.
* The ~~WebTest~~ examples illustrate how to use [WebTest](http://webtest.canoo.com/) to test the blogging application. (Moved to the archive branch)
* The __SeleniumServer__ examples illustrate how to use [Selenium Server](http://seleniumhq.org/projects/remote-control/) (previously called Selenium RC or sometimes referred to as Selenium 1) to test the blogging application.
* The __WebDriver__ examples illustrate how to use [Selenium WebDriver](http://seleniumhq.org/projects/webdriver/) (sometimes referred to as Selenium 2) to test the blogging application.
* The __HttpBuilder__ examples illustrate how to use [HttpBuilder](https://github.com/jgritman/httpbuilder/) as the test 'driver' to test the blogging application.
* The __HttpBuilderNG__ examples illustrate how to use [HttpBuilder-NG](https://github.com/http-builder-ng/http-builder-ng/) as the test 'driver' to test the blogging application.
This is a fairly low-level api - it gives easy access to status codes, cookies and other low-level information if you require it.

Towards more maintainable Tests
-------------------------------

* The __Geb__ and __WebDriver__ examples illustrate ways to separate the concerns of user interface (or view) and logical model.
This is done by writing tests in terms of a logical model and as a separate activity defining the user interface aspects
corresponding to the model. One technique illustrated is the use of _Page_ objects.

Towards more readable Tests
---------------------------

* The __Spock__ examples illustrate how to use [Spock](http://spockframework.org/) to test the blogging application.
* The __EasyB__ examples illustrate how to use [EasyB](https://github.com/easyb) to test the blogging application.
* The __JBehave__ examples illustrate how to use [JBehave](http://jbehave.org/) to test the blogging application.
* The __CucumberJava__ and __CucumberGroovy__ examples illustrate how to use [Cucumber](https://cucumber.io/) to test the blogging application.
* The __HtmlUnit__ examples illustrate some *fluent api* and *DSL* usage.
* The ~~Slim~~ examples illustrate writing your tests FitNesse style in a wiki with the fixture code using Groovy. (Moved to the archive branch)
These examples also illustrate a number of FitNesse related tools including *HtmlFixture*, *Xebium* and *ZiBreve*.
* The __Concordion__ examples illustrate how to use [Concordion](http://concordion.org/) to test the blogging application.
* The __RobotFramework__ examples illustrate how to use [Robot Framework](http://code.google.com/p/robotframework/) to test the blogging application.
* The __Tumbler__ example illustrates using the [Tumbler](https://github.com/berkus/tumbler-glass) BDD tool.
* The __Serenity__ examples illustrate using the [Serenity](http://thucydides.info/) BDD tool.

Advanced Test Case/Data Selection
---------------------------------

* The __DataDriven__ examples illustrate the data-driven capabilities of [JUnit4](http://www.junit.org/) and [TestNG](http://testng.org).
* The __CombinationsAndPairs__ examples examine the idea of testing combinations of parameters where the combinations are automatically generated.
Also looks at the idea of [pairwise testing](http://www.pairwise.org/) (also known as orthogonal array testing or all pairs testing).
* The __Choco__ examples illustrate how to use [Choco](http://choco.emn.fr/) to test the blogging application using conditions expressed as logic constraints.
* The __ModelJUnit__ examples illustrate how to use [ModelJUnit](http://www.cs.waikato.ac.nz/~marku/mbt/modeljunit/) to generate test cases
from an 'external' model of an application/system.
* The __PropertyBased__ examples illustrate how to use [QuickCheck](http://java.net/projects/quickcheck) and similar libraries to generate test cases
using random and generated data values.
* The __GPars__ examples illustrate how to run tests in parallel

Other useful testing tools
--------------------------

* The __Ersatz__ example uses the [Ersatz](http://stehno.com/ersatz/) http proxy instead of a real http server
* The __Dyna4jdbc__ example uses the [Dyna4jdbc](https://github.com/peter-gergely-horvath/dyna4jdbc) scripted database driver

Requirements
------------

Required:

* This lab assumes you have JDK8 installed. If you don't already have it, see the links below.

Recommended:

* An IDE such as Intellij IDEA (see links below)
* If not using an IDE with Git built-in, command-line Git is recommended (alternatively download the associated zip with this project)

Other useful tools
------------------

* [Java](http://www.oracle.com/technetwork/java/javase/downloads) Required but may be already installed on your system
* [Groovy](http://groovy-lang.org/download.html) Groovy is required for the examples but is generally added as a dependency and known by the IDE/Gradle. A standalone Groovy install may optionally be installed if you don't already have one.
* [IntelliJ IDEA](http://www.jetbrains.com/idea/) Optional IDE (Community edition includes Groovy support)
* [Eclipse IDE with Groovy support](https://github.com/groovy/groovy-eclipse/wiki) Optional IDE (see also: https://github.com/groovy/groovy-eclipse)
* [Graphviz/dot](http://www.graphviz.org/) open source graph visualization/diagram software.

Hints for using
---------------

Firstly, clone this repo from github.
At the command prompt with Java in the path, type `./gradlew idea`. (Windows users just use `gradlew idea`.)
Now, open the generated `MakeTestingGroovy.ipr` in IntelliJ IDEA and run
the "test" examples from within IntelliJ IDEA unless otherwise noted in individual projects README files.
Eclipse users might want to try `./gradlew eclipse` but this isn't fully tested.
