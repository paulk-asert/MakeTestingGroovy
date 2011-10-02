import com.gargoylesoftware.htmlunit.WebClient
import junit.framework.TestCase

@Grab('net.sourceforge.htmlunit:htmlunit:2.9')
@GrabExclude('xml-apis:xml-apis')
class TestSimpBlogJUnit extends TestCase {
    def page

    void setUp() {
        page = new WebClient().getPage('http://localhost:8080/postForm')
        // check page title
        assert 'Welcome to SimpBlog' == page.titleText
    }

    void testBartWasHere() {
        // fill in blog entry and post it
        def form = page.getFormByName('post')
        form.getInputByName('title').setValueAttribute('Bart was here (HtmlUnit JUnit3)')
        form.getSelectByName('category').getOptions().find {
            it.text == 'Home' }.setSelected(true)
        form.getTextAreaByName('content').setText('Cowabunga Dude!')
        def result = form.getInputByName('btnPost').click()

        // check blog post details
        assert result.getElementsByTagName('h1').item(0).
                textContent.matches('Post.*: Bart was here.*')
        def h3headings = result.getElementsByTagName('h3')
        assert h3headings.item(1).textContent == 'Category: Home'
        assert h3headings.item(2).textContent == 'Author: Bart'

        // expecting: <table><tr><td><p>Cowabunga Dude!</p></td></tr></table>
        def cell = result.getByXPath('//TABLE//TR/TD')[0]
        def para = cell.getFirstChild()
        assert para.textContent == 'Cowabunga Dude!'
    }
}