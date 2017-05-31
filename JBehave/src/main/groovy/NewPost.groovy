//@Grab('org.jbehave:jbehave-core:4.1.1')
//@GrabConfig(systemClassLoader = true)
//@Grab('xml-apis:xml-apis:1.4.01')
import org.jbehave.core.junit.JUnitStory
import org.jbehave.core.configuration.Configuration
import org.jbehave.core.configuration.MostUsefulConfiguration
import org.jbehave.core.io.LoadFromRelativeFile
import org.jbehave.core.reporters.StoryReporterBuilder
import org.jbehave.core.steps.InstanceStepsFactory
import static org.jbehave.core.reporters.Format.*

class NewPost extends JUnitStory {
    Configuration configuration() {
        return new MostUsefulConfiguration()
            .useStoryLoader(new LoadFromRelativeFile(new File('src/main/groovy').toURL()))
            .useStoryReporterBuilder(new StoryReporterBuilder().withDefaultFormats().withFormats(TXT, HTML, CONSOLE))
    }
 
    List candidateSteps() {
        return new InstanceStepsFactory(configuration(), new NewPostSteps()).createCandidateSteps()
    }
}