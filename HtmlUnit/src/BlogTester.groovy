import com.gargoylesoftware.htmlunit.WebClient
import java.util.regex.Pattern

class BlogTester {
    def page
    def lastResult

    BlogTester(String url) {
        page = new WebClient().getPage(url)
    }

    def checkTitle(String title) {
        assert title == page.titleText
    }

    def postBlog(Map params) {
        def form = page.getFormByName('post')
        form.getInputByName('title').setValueAttribute(params.title)
        form.getSelectByName('category').options.find {
            it.text == params.category }.setSelected(true)
        form.getSelectByName('author').options.find {
            it.text == params.author }.setSelected(true)
        form.getTextAreaByName('content').setText(params.content)
        lastResult = form.getInputByName('btnPost').click()
    }

    def checkHeadingMatches(String regex) {
        assert lastResult.getElementsByTagName('h1').item(0).
                textContent.matches(regex.replaceAll('([()])', '\\\\$1'))
    }

    def checkSubheading(String prefix, String suffix) {
        def h3headings = lastResult.getElementsByTagName('h3')
        def h3textNode = h3headings.nodes.find{
            it.firstChild.textContent.startsWith(prefix) }.firstChild
        assert h3textNode.textContent.endsWith(suffix)
    }

    def checkPostText(String text) {
        // expecting: <table><tr><td><p>text</p></td></tr></table>
        def cell = lastResult.getByXPath('//TABLE//TR/TD')[0]
        def para = cell.firstChild
        assert para.textContent == text
    }

    def postAndCheck(title, category, author, content) {
        checkTitle 'Welcome to SimpBlog'
        postBlog title: title, category: category, content: content, author: author
        checkHeadingMatches "Post.*: $title.*"
        checkSubheading 'Category', category
        checkSubheading 'Author', author
        checkPostText content
    }
}
