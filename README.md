Making Testing Groovy
=====================

This project looks at a simple blog application and numerous tools and techniques to test it. In that sense, the examples
focus on web testing (and to some degree acceptance testing) - having said that, many of the techniques are generally
applicable and can be applied to any kind of testing for any kind of application or system. The examples are mostly
[Groovy](http://groovy-lang.org/) based but again, the ideas transfer across to some degree to other modern languages.
The examples are designed to be runnable 'out-of-the-box' by cutting and pasting the code into the GroovyConsole which
comes with any typical Groovy installation. Alternatively, they can be run from your favorite IDE (tested with IDEA
community edition and, to a lesser degree, Eclipse).

Application Under Test
----------------------

The application is a simple web-based blogging system. It is a little novel in that it combines in a single
Groovy file a Spring/standalone GORM/Jetty application. Just type 'groovy SimpBlog' in the appropriate directory
and your application will start. Or you can fire it up in your favorite IDE.

Basic Test Automation
---------------------

* The __Vanilla__ examples illustrate how to test the blogging application using basic low-level Groovy/Java features. No testing framework is used.
* The __HtmlUnit__ examples illustrate how to use [HtmlUnit](http://htmlunit.sourceforge.net/) as the test 'driver' to test the blogging application.
Various test 'runners' are illustrated: plain groovy, [JUnit](http://www.junit.org/) 3/4/5, [TestNG](http://testng.org)
and GroovyTestCase. Various enhancements are examined: a test case abstraction, a fluent test api and a testing DSL.
* The __JWebUnit__ examples illustrate how to use [JWebUnit](http://jwebunit.sourceforge.net/) as the test 'driver' to test the blogging application.
* The __Watij__ examples illustrate how to use [Watij](http://watij.com) to test the blogging application. Uses the 2010 vintage [webspec-api](http://watij.com/webspec-api/)
which supports IE, Firefox/Mozilla and Safari.
* The __Geb__ examples illustrate how to use [Geb](http://www.gebish.org/) to test the blogging application.
* The __WebTest__ examples illustrate how to use [WebTest](http://webtest.canoo.com/) to test the blogging application.
* The __SeleniumServer__ examples illustrate how to use [Selenium Server](http://seleniumhq.org/projects/remote-control/) (previously called Selenium RC or sometimes referred to as Selenium 1) to test the blogging application.
* The __WebDriver__ examples illustrate how to use [Selenium WebDriver](http://seleniumhq.org/projects/webdriver/) (sometimes referred to as Selenium 2) to test the blogging application.
* The __HttpBuilder__ examples illustrate how to use [HttpBuilder](http://groovy.codehaus.org/modules/http-builder/) as the test 'driver' to test the blogging application.
This is a fairly low-level api - it gives easy access to status codes, cookies and other low-level information if you require it.

Towards more maintainable Tests
-------------------------------

* The __Geb__, __WebDriver__ and --Tellurium-- examples illustrate ways to separate the concerns of user interface (or view) and logical model.
This is done by writing tests in terms of a logical model and as a separate activity defining the user interface aspects
corresponding to the model. One technique illustrated is the use of _Page_ objects.

Towards more readable Tests
---------------------------

* The __Spock__ examples illustrate how to use [Spock](http://code.google.com/p/spock/) to test the blogging application.
* The __EasyB__ examples illustrate how to use [EasyB](http://www.easyb.org/) to test the blogging application.
* The __JBehave__ examples illustrate how to use [JBehave](http://jbehave.org/) to test the blogging application.
* The __HtmlUnit__ examples illustrate some *fluent api* and *DSL* usage.
* The __Slim__ examples illustrate writing your tests FitNesse style in a wiki with the fixture code using Groovy.
These examples also illustrate a number of FitNesse related tools including *HtmlFixture*, *Xebium* and *ZiBreve*.
* The __Concordion__ examples illustrate how to use [Concordion](http://concordion.org/) to test the blogging application.
* The __RobotFramework__ examples illustrate how to use [Robot Framework](http://code.google.com/p/robotframework/) to test the blogging application.

Advanced Test Case/Data Selection
---------------------------------

* The __DataDriven__ examples illustrate the data-driven capabilities of [JUnit4](http://www.junit.org/) and [TestNG](http://testng.org).
* The __CombinationsAndPairs__ examples examine the idea of testing combinations of parameters where the combinations are automatically generated.
Also looks at the idea of [pairwise testing](http://www.pairwise.org/) (also known as orthogonal array testing or all pairs testing).
* The __Choco__ examples illustrate how to use [Choco](http://choco.emn.fr/) to test the blogging application using conditions expressed as logic constraints.
* The __ModelJUnit__ examples illustrate how to use [ModelJUnit](http://www.cs.waikato.ac.nz/~marku/mbt/modeljunit/) to generate test cases
from an 'external' model of an application/system.
* The __QuickCheck__ examples illustrate how to use [QuickCheck](http://java.net/projects/quickcheck) to generate test cases
using random and generated data values.
* The __GPars__ examples illustrate how to run tests in parallel

Other useful tools (optional)
-----------------------------

* [Java](http://www.oracle.com/technetwork/java/javase/downloads) Required but may be already installed on your system
* [IntelliJ IDEA](http://www.jetbrains.com/idea/) IDE (Community edition includes Groovy support)
* [Eclipse IDE](http://www.eclipse.org/downloads/) (plus download the [Groovy plugin](http://groovy.codehaus.org/Eclipse+Plugin))
* [SpringSource Tool Suite](http://www.springsource.com/developer/sts) STS is SpringSource's bundled Eclipse IDE
* [Graphviz/dot](http://www.graphviz.org/) open source graph visualization/diagram software.

Hints for using
---------------

Firstly, clone this repo or download the associated zip from github. At the comand prompt with Java
in the path, type `./gradlew idea`. (Windows users just use `gradlew idea`.)
Now, open the generated `MakeTestingGroovy.ipr` in IntelliJ IDEA and run
the "test" examples from within IntelliJ IDEA unless otherwise noted in individual projects README files.
Eclipse users might want to try `./gradlew eclipse` but this isn't fully tested.