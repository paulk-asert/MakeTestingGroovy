//@Grab('org.jbehave:jbehave-core:4.1.1')
//@GrabConfig(systemClassLoader = true)
//@Grab('xml-apis:xml-apis:1.4.01')
import org.jbehave.core.embedder.Embedder
import org.jbehave.core.io.CodeLocations
import org.jbehave.core.io.LoadFromClasspath
import org.jbehave.core.io.StoryFinder
import org.jbehave.core.junit.JUnitStory
import org.jbehave.core.configuration.Configuration
import org.jbehave.core.configuration.MostUsefulConfiguration
import org.jbehave.core.reporters.StoryReporterBuilder
import org.jbehave.core.steps.InstanceStepsFactory
import org.junit.Test

import static org.jbehave.core.reporters.Format.*

class NewPost extends JUnitStory {
    Configuration configuration() {
        new MostUsefulConfiguration()
            .useStoryLoader(new LoadFromClasspath(getClass()))
            .useStoryReporterBuilder(
                new StoryReporterBuilder()
                        .withDefaultFormats()
                        .withCodeLocation(CodeLocations.codeLocationFromPath("build/jbehave"))
                        .withFormats(TXT, HTML, CONSOLE))
    }
 
    List candidateSteps() {
        new InstanceStepsFactory(configuration(), new NewPostSteps()).createCandidateSteps()
    }

    @Test
    void mapStories() {
        // create a dummy story map since built-in template assumes we have one
        configuredEmbedder().mapStoriesAsPaths([])
    }
}