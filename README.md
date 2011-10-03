Making Testing Groovy
=====================

This project looks at a simple blog application and numerous tools and techniques to test it. In that sense, the examples
focus on web testing (and to some degree acceptance testing) - having said that, many of the techniques are generally
applicable and can be applied to any kind of testing for any kind of application or system. The examples are mostly
Groovy based but again, the ideas transfer across to some degree to other modern languages. The examples are
designed to be runnable 'out-of-the-box' by cutting and pasting the code into the GroovyConsole which comes
with any typical Groovy installation. Alternatively, they can be run from your favorite IDE (tested with IDEA
community edition and Eclipse).

Application Under Test
----------------------

The application is a simple web-based blogging system. It is a little novel in that it combines in a single
Groovy file a Spring/standalone GORM/Jetty application. Just type 'groovy SimpBlog' in the appropriate directory
and your application will start. Or you can fire it up in your favorite IDE.

HtmlUnit
--------

Illustrates how to use HtmlUnit as the test 'driver' to test the blogging application.
Various test 'runners' are illustrated: plain groovy, JUnit3/4, TestNG, GroovyTestCase.
Various enhancements are examined: a test case abstraction, a fluent test api, a testing DSL

DataDriven
----------

Builds upon the HtmlUnit test case abstraction but adds data-driven capabilities.
Illustrates using JUnit4 and TestNG.

CombinationsAndPairs
--------------------

Examines the idea of testing combinations of parameters where the combinations are automatically generated.
Also looks at the idea of pairwise testing (also known as orthogonal array testing or all pairs testing).

JWebUnit
--------

Illustrates how to use JWebUnit as the test 'driver' to test the blogging application.

HttpBuilder
-----------

Illustrates how to use HttpBuilder as the test 'driver' to test the blogging application.
This is a fairly low-level api - it gives easy access to status codes, cookies and other
low-level information if you require it.

groovy-1.8.2
------------

This is an embedded copy of the Groovy programming language. You don't need this if you already have Groovy installed.