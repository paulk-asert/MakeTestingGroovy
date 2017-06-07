JBehave
=======

JBehave is a BDD style testing framework. Stories are written in text files
and then glue code (here written in Groovy) is used to map the story text
into appropriate code that exercises the application under test.

It is recommended that you use gradle to run the tests using:

`./gradlew :JBehave:test`

If you run from within your IDE you will find that the tests are run but
various images and style files that the HTML reports use aren't set up,
so it will look a little strange.
