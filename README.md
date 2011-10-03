Making Testing Groovy
=====================

This project looks at a simple blog application and numerous tools and techniques to test it. In that sense, the examples
focus on web testing (and to some degree acceptance testing) - having said that, many of the techniques are generally
applicable and can be applied to any kind of testing for any kind of application or system. The examples are mostly
[Groovy](http://groovy.codehaus.org/) based but again, the ideas transfer across to some degree to other modern languages.
The examples are designed to be runnable 'out-of-the-box' by cutting and pasting the code into the GroovyConsole which
comes with any typical Groovy installation. Alternatively, they can be run from your favorite IDE (tested with IDEA
community edition and Eclipse).

Application Under Test
----------------------

The application is a simple web-based blogging system. It is a little novel in that it combines in a single
Groovy file a Spring/standalone GORM/Jetty application. Just type 'groovy SimpBlog' in the appropriate directory
and your application will start. Or you can fire it up in your favorite IDE.

HtmlUnit
--------

Illustrates how to use [HtmlUnit](http://htmlunit.sourceforge.net/) as the test 'driver' to test the blogging application.
Various test 'runners' are illustrated: plain groovy, [JUnit](http://www.junit.org/) 3/4, [TestNG](http://testng.org)
and GroovyTestCase. Various enhancements are examined: a test case abstraction, a fluent test api and a testing DSL.

DataDriven
----------

Builds upon the HtmlUnit test case abstraction but adds data-driven capabilities.
Illustrates using [JUnit4](http://www.junit.org/) and [TestNG](http://testng.org).

CombinationsAndPairs
--------------------

Examines the idea of testing combinations of parameters where the combinations are automatically generated.
Also looks at the idea of [pairwise testing](http://www.pairwise.org/) (also known as orthogonal array testing or all pairs testing).

JWebUnit
--------

Illustrates how to use [JWebUnit](http://jwebunit.sourceforge.net/) as the test 'driver' to test the blogging application.

HttpBuilder
-----------

Illustrates how to use [HttpBuilder](http://groovy.codehaus.org/modules/http-builder/) as the test 'driver' to test the blogging application.
This is a fairly low-level api - it gives easy access to status codes, cookies and other low-level information if you require it.

ModelJUnit
----------

Illustrates how to use [ModelJUnit](http://www.cs.waikato.ac.nz/~marku/mbt/modeljunit/) to generate test cases
from an 'external' model of an application/system.

QuickCheck
----------

Illustrates how to use [QuickCheck](http://java.net/projects/quickcheck) to generate test cases
using random and generated data values.

groovy-1.8.2
------------

This is an embedded copy of the Groovy programming language. You don't need this if you already have Groovy installed.

Other useful tools (optional)
-----------------------------

* [IntelliJ IDEA](http://www.jetbrains.com/idea/) IDE (Community edition includes Groovy support)
* [Eclipse IDE](http://www.eclipse.org/downloads/) (plus download the [Groovy plugin](http://groovy.codehaus.org/Eclipse+Plugin))
* [Graphviz/dot](http://www.graphviz.org/) open source graph visualization/diagram software.
