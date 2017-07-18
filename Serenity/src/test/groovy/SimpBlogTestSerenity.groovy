import net.serenitybdd.junit.runners.SerenityRunner
import net.thucydides.core.annotations.Steps
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(SerenityRunner)
class SimpBlogTestSerenity {
    @Steps
    SimpBlogSteps steps

    @Test
    void shouldCorrectlyPostBlog() {
        // GIVEN
        steps.gotoEntryPage()

        // WHEN
        steps.enterTitle('Bart was here')
        steps.enterContent('Cowabunga Dude!')
        steps.selectOption('Home', 'category')
        steps.selectOption('Bart', 'author')
        steps.clickPostButton()

        // THEN
        steps.checkPost('Bart was here')
    }
}
