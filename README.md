Making Testing Groovy
=====================

This project looks at a simple blog application and numerous tools and techniques to test it.

Application Under Test
----------------------

The application is a simple web-based blogging system.

HtmlUnit
--------

Illustrates how to use HtmlUnit as the test 'driver' to test the blogging application.
Various test 'runners' are illustrated: plain groovy, JUnit3/4, TestNG, GroovyTestCase.
Various enhancements are examined: a test case abstraction, a fluent test api, a testing DSL

DataDriven
----------

Builds upon the HtmlUnit test case abstraction but adds data-driven capabilities.
Illustrates using JUnit4 and TestNG.

HttpBuilder
-----------

Illustrates how to use HttpBuilder as the test 'driver' to test the blogging application.
This is a fairly low-level api - it gives easy access to status codes, cookies and other low-level information if you require it.

