RobotFramework
==============

RobotFramework uses Python to run ATDD tests from various formats.
The fact that python is being used is totally hidden from us.
In our case, Jython is used since we are on the JVM but the
test cases are just text files (see the documentation for other
supported formats) and our glue code (for mapping tests to the
application under test)is just Groovy.

It is recommended you run this project use gradle rather than your IDE; otherwise,
the report directory location might be determined by what working directory your
IDE uses when running the file.

To use gradle, the predefined `run` task can be used:

```
./gradlew :RobotFramework:run
```

Further info:
* http://robotframework.org/
