WebDriver
=========

It is recommended you run this project use gradle rather than your IDE; this
is of particular importance if you are getting linkage errors due to multiple xml-api
jar versions on the classpath when using IntelliJ.

To use gradle, the predefined `run` and `runPages` tasks can be used.

You can configure the test scripts to use the HtmlUnit browser emulator (the default) or a real
browser via an appropriate driver. There are some comments in the source and build
files to give you hints for several browsers but also see the Selenium WebDriver
documentation for other browsers or more details.
