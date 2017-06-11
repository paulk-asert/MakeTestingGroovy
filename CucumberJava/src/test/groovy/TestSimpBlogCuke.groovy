import cucumber.api.CucumberOptions
import cucumber.api.junit.Cucumber
import org.junit.runner.RunWith

@RunWith(Cucumber)
@CucumberOptions(features=['src/test/resources'],
        plugin=['html:build/reports/cucumber'])
class TestSimpBlogCuke {
    // meant to be blank
}
