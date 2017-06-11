import net.serenitybdd.junit.runners.SerenityRunner
import net.thucydides.core.annotations.Steps
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(SerenityRunner)
class HelloTest {
    @Steps
    HelloSteps helloSteps

    @Test
    void shouldAddOnePlusOne() {
        // GIVEN
        helloSteps.start_with(1)

        // WHEN
        helloSteps.add(1)

        // THEN
        helloSteps.result_should_be(2)
    }
}
