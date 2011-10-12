This directory illustrates using Slim/FitNesse with the Fixture files written in Groovy.

The gradle build file will compile the fixture files and assemble the necessary libraries by using the
following command (shown for windows - switch the slash around for most other operating systems):

    > ..\gradlew dist

Fitnesse can be run manually as follows (shown running on port 9090 which can be changed if needed):

    > cd fitnesse
    > java -jar finesse.jar -p 9090

There is a shortcut to run fitnesse via gradle (but standard output is hidden if doing this)
which will automatically do both the above steps:

    > ..\gradlew runFitnesse
