import com.gargoylesoftware.htmlunit.WebClient

class BlogTestCase extends GroovyTestCase {
    def page
    def lastResult

    void setUp() {
        page = new WebClient().getPage('http://localhost:8080/postForm')
    }

    def checkTitle(String title) {
        assert title == page.titleText
    }

    def prepareBlog() {
        new PrepareBlogEmpty()
    }

    def postBlog(Map params) {
        def form = page.getFormByName('post')
        form.getInputByName('title').setValueAttribute(params.title)
        form.getSelectByName('category').options.find {
            it.text == params.category
        }.setSelected(true)
        form.getSelectByName('author').options.find {
            it.text == params.author
        }.setSelected(true)
        form.getTextAreaByName('content').setText(params.content)
        lastResult = form.getInputByName('btnPost').click()
    }

    def checkHeadingMatches(String regex) {
        assert lastResult.getElementsByTagName('h1').item(0).textContent.matches(regex)
    }

    def checkSubheading(int index, String text) {
        def h3headings = lastResult.getElementsByTagName('h3')
        assert h3headings.item(index).textContent == text
    }

    def checkPostText(String text) {
        // expecting: <table><tr><td><p>text</p></td></tr></table>
        def cell = lastResult.getByXPath('//TABLE//TR/TD')[0]
        def para = cell.getFirstChild()
        assert para.textContent == text
    }

    class PrepareBlogEmpty {
        PrepareBlogTitle withTitle(title) {
            new PrepareBlogTitle([title: title])
        }
    }

    class PrepareBlogTitle {
        Map params

        PrepareBlogTitle(Map params) {
            this.params = params
        }

        PrepareBlogTitleAuthor withAuthor(author) {
            new PrepareBlogTitleAuthor(params + [author: author])
        }
    }

    class PrepareBlogTitleAuthor {
        Map params

        PrepareBlogTitleAuthor(Map params) {
            this.params = params
        }

        PrepareBlogTitleAuthorCategory withCategory(category) {
            new PrepareBlogTitleAuthorCategory(params + [category: category])
        }
    }

    class PrepareBlogTitleAuthorCategory {
        Map params

        PrepareBlogTitleAuthorCategory(Map params) {
            this.params = params
        }

        PrepareBlogReady withContent(content) {
            new PrepareBlogReady(params + [content: content])
        }
    }

    class PrepareBlogReady {
        Map params

        PrepareBlogReady(Map params) {
            this.params = params
        }

        def post() {
            postBlog(params)
        }
    }
}
