import org.junit.runner.RunWith

import fitnesse.junit.FitNesseSuite
import fitnesse.junit.FitNesseSuite.FitnesseDir
import fitnesse.junit.FitNesseSuite.Name
import fitnesse.junit.FitNesseSuite.OutputDir

@RunWith(FitNesseSuite)
@Name("FitNesse.SimpBlogSuite.SimpBlogTest")
@FitnesseDir("Slim/fitnesse")
@OutputDir(systemProperty = "java.io.tmpdir", pathExtension = "fitnesse")
class FitNesseSuiteJUnitRunnerTest {
//    @After
//    void cleanup() {
//        def temp = new File(System.getProperty('java.io.tmpdir'), 'fitnesse')
//        println "Results can be found here: ${new File(temp, 'FitNesse.SimpBlogSuite.SimpBlogTest.html')}"
//    }
}

