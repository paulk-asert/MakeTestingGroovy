//@Grab('net.sourceforge.jwebunit:jwebunit-core:3.3')
//@Grab('net.sourceforge.jwebunit:jwebunit-htmlunit-plugin:3.3')
//@GrabExclude('xml-apis:xml-apis')
import net.sourceforge.jwebunit.junit.*

class TestSimpBlog extends WebTestCase {

    void setUp() {
        setBaseUrl("http://localhost:8080")
    }

    void testPostBlog() {
        beginAt "/postForm"
        assertTitleEquals "Welcome to SimpBlog"

        setTextField "title", "Bart was here (JWebUnit)"
        setTextField "content", "Cowabunga Dude!"
        selectOption "category", "Home"
        clickButtonWithText "Create Post"

        assert getElementByXPath('//H1').textContent.matches('Post.*: Bart was here.*')
        def h3headings = getElementsByXPath('//H3')
        assert h3headings[1].textContent == "Category: Home"
        assert h3headings[2].textContent == "Author: Bart"
        def cell = getElementByXPath('//TABLE//TR/TD')
        assert cell.children[0].textContent == 'Cowabunga Dude!'
    }
}
