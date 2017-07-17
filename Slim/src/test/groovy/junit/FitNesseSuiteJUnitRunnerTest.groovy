import org.junit.runner.RunWith

import fitnesse.junit.FitNesseRunner

@RunWith(FitNesseRunner)
@FitNesseRunner.Suite("FitNesse.SimpBlogSuite.SimpBlogTest")
@FitNesseRunner.FitnesseDir("Slim/fitnesse")
@FitNesseRunner.OutputDir("build/results")
class FitNesseSuiteJUnitRunnerTest {
//    @After
//    void cleanup() {
//        def temp = new File(System.getProperty('java.io.tmpdir'), 'fitnesse')
//        println "Results can be found here: ${new File(temp, 'FitNesse.SimpBlogSuite.SimpBlogTest.html')}"
//    }
}

