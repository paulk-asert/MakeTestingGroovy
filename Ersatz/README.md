Ersatz
======

Erstaz is a HTTP proxy useful for testing. You use it in the same way as
a mocking package for Java, i.e. by setting expected results for particular
HTTP requests.

Our test is a JUnit 4 test. The expectations are set within the `@Before` annotated method.
Our test code itself remains essentially the same as for other low-level tests in
this repo (i.e. using htmlunit directly) but we don't test against port 8080 but
using the ersatz default port (accessed via `server.httpUrl`). After running our test
code we check that the proxy expectations were not violated, using `server.verify()`,
before doing some cleanup in the `@After` annotated method.

Further info:
* http://stehno.com/ersatz/
