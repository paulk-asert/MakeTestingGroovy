//@Grab('net.sourceforge.htmlunit:htmlunit:2.26')
//@GrabExclude('xml-apis:xml-apis')
import com.gargoylesoftware.htmlunit.Page
import com.gargoylesoftware.htmlunit.WebClient
import com.gargoylesoftware.htmlunit.html.*
import groovy.transform.CompileStatic

@CompileStatic
def test() {
    WebClient client = new WebClient()
    Page rawPage = client.getPage('http://localhost:8080/postForm')
    assert rawPage instanceof HtmlPage
    HtmlPage page = (HtmlPage) rawPage
    assert 'Welcome to SimpBlog' == page.titleText

    // fill in blog entry and post it
    HtmlForm form = page.getFormByName('post')
    form.getInputByName('title').
            setValueAttribute('Bart was here (HtmlUnit)')
    form.getSelectByName('category').getOptions().find{
        it.text == 'School' }.setSelected(true)
    form.getTextAreaByName('content').setText('Cowabunga Dude!')
    Page rawResult = form.getInputByName('btnPost').click()
    assert rawResult instanceof HtmlPage
    HtmlPage result = (HtmlPage) rawResult

    // check blog post details
    assert result.getElementsByTagName('h1').item(0).
            textContent.matches('Post.*: Bart was here.*')
    DomNodeList h3headings = result.getElementsByTagName('h3')
    assert h3headings.item(1).textContent == 'Category: School'
    assert h3headings.item(2).textContent == 'Author: Bart'

    // expecting: <table><tr><td><p>Cowabunga Dude!</p>...</table>
    HtmlTableDataCell cell = result.getByXPath('//TABLE//TR/TD')[0]
    assert cell.textContent.trim() == 'Cowabunga Dude!'
}

test()
