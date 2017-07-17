//@Grab('net.sourceforge.htmlunit:htmlunit:2.27')
//@GrabExclude('xml-apis:xml-apis')
import com.gargoylesoftware.htmlunit.WebClient

class TestSimpBlogGUnit extends GroovyTestCase {
    def page

    void setUp() {
        page = new WebClient().getPage('http://localhost:8080/postForm')
        // check page title
        assert 'Welcome to SimpBlog' == page.titleText
    }

    void testBartWasHere() {
        // fill in blog entry and post it
        def form = page.getFormByName('post')
        form.getInputByName('title').setValueAttribute('Bart was here (HtmlUnit GUnit)')
        form.getSelectByName('category').getOptions().find {
            it.text == 'School' }.setSelected(true)
        form.getTextAreaByName('content').setText('Cowabunga Dude!')
        def result = form.getInputByName('btnPost').click()

        // check blog post details
        assert result.getElementsByTagName('h1').item(0).
                textContent.matches('Post.*: Bart was here.*')
        def h3headings = result.getElementsByTagName('h3')
        assert h3headings.item(1).textContent == 'Category: School'
        assert h3headings.item(2).textContent == 'Author: Bart'

        // expecting: <table><tr><td><p>Cowabunga Dude!</p></td></tr></table>
        def cell = result.getByXPath('//TABLE//TR/TD')[0]
        assert cell.textContent.trim() == 'Cowabunga Dude!'
    }
}