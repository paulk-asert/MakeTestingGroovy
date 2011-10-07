//@Grab('org.jbehave:jbehave-core:3.5.1')
import org.jbehave.core.junit.JUnitStory
import org.jbehave.core.configuration.Configuration
import org.jbehave.core.configuration.MostUsefulConfiguration
import org.jbehave.core.io.LoadFromRelativeFile
import org.jbehave.core.reporters.StoryReporterBuilder
import org.jbehave.core.reporters.Format
import org.jbehave.core.steps.InstanceStepsFactory

class NewPost extends JUnitStory {
    Configuration configuration() {
        return new MostUsefulConfiguration()
            .useStoryLoader(new LoadFromRelativeFile(new File('JBehave/src').toURL()))
            .useStoryReporterBuilder(new StoryReporterBuilder().withDefaultFormats().withFormats(Format.CONSOLE))
    }
 
    List candidateSteps() {
        return new InstanceStepsFactory(configuration(), new NewPostSteps()).createCandidateSteps()
    }
}