import com.gargoylesoftware.htmlunit.WebClient

class BlogTesterBoolean {
    def page
    def lastResult

    BlogTesterBoolean(String url) {
        page = new WebClient().getPage(url)
    }

    boolean checkTitle(String title) {
        title == page.titleText
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

    boolean checkHeadingMatches(String suffix) {
        def h1 = lastResult.getElementsByTagName('h1').item(0).textContent
        h1.matches("(?s)Post \\d*: .*") && h1.endsWith(suffix)
    }

    boolean checkSubheading(String prefix, String suffix) {
        def h3headings = lastResult.getElementsByTagName('h3')
        def h3textNode = h3headings.find{
            it.firstChild.textContent.startsWith(prefix) }.firstChild
        h3textNode.textContent.endsWith(suffix)
    }

    boolean checkPostText(String text) {
        // expecting: <table><tr><td><p>text</p></td></tr></table>
        def cell = lastResult.getByXPath('//TABLE//TR/TD')[0]
        cell.textContent.trim() == text.trim()
    }

    boolean checkAll(title, category, author, content) {
        checkHeadingMatches(title) &&
        checkSubheading('Category', category) &&
        checkSubheading('Author', author) &&
        checkPostText(content)
    }
}
