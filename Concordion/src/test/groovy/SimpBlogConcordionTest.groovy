//@Grab('net.sourceforge.htmlunit:htmlunit:2.27')
//@Grab('org.concordion:concordion:2.1.0')
//@GrabExclude('xml-apis:xml-apis')
import org.concordion.integration.junit4.ConcordionRunner
import com.gargoylesoftware.htmlunit.WebClient
import org.junit.runner.RunWith

@RunWith(ConcordionRunner)
class SimpBlogConcordionTest {
    String resultingHeadingForBlog(title, author, category, content) {
        def client = new WebClient()
        def page = client.getPage('http://localhost:8080/postForm')
        assert 'Welcome to SimpBlog' == page.titleText
        def form = page.getFormByName('post')
        form.getInputByName('title').setValueAttribute("$title (Concordion)")
        form.getSelectByName('author').getOptions().find{ it.text == author }.setSelected(true)
        form.getSelectByName('category').getOptions().find{ it.text == category }.setSelected(true)
        form.getTextAreaByName('content').setText(content)
        def result = form.getInputByName('btnPost').click()
        def m = result.getElementsByTagName('h1').item(0).textContent =~ /Post \d+: (.*) \([^)]*\)/
        m[0][1]
    }
}
