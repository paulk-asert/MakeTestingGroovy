//@Grab('com.stehno.ersatz:ersatz:1.2.0')
//@Grab('net.sourceforge.htmlunit:htmlunit:2.26')
//@GrabExclude('xml-apis:xml-apis')
import com.gargoylesoftware.htmlunit.WebClient
import com.stehno.ersatz.ContentType
import com.stehno.ersatz.ErsatzServer
import org.junit.After
import org.junit.Before
import org.junit.Test

class SimpBlogErsatzTest {
    ErsatzServer server
    private static final formHtml = '''
    <html>
    <head>
    <title>Welcome to SimpBlog</title>
    </head>
    <body>
        <form name='post' action='/addPost'>
            <input type='text' name='title' />
            <select name='author'>
              <option value='1'>Bart</option>
            </select>
            <select name='category'>
              <option value='1'>School</option>
            </select>
            <textarea name='content' rows='4' cols='60'></textarea>
            <input type='submit' name='btnPost' value='Create Post' />
        </form>
    </body>
    </html>
    '''
    private static final doneHtml = '''
    <html>
    <body>
    <h1>Post 99: Bart was here (Ersatz)</h1>
    </body>
    </html>
    '''
    private static final postParams = [
            author: '1',
            category: '1',
            title: 'Bart was here (Ersatz)',
            content: 'Cowabunga Dude!',
            btnPost: 'Create Post'
    ]

    @Before
    void startProxyServer() {
        server = new ErsatzServer()
        server.expectations {
            get('/postForm') {
                called(1)
                responds().content(formHtml, ContentType.TEXT_HTML)
            }
            get('/addPost') {
                called(1)
                postParams.each { k, v ->
                    query(k, v)
                }
                responds().content(doneHtml, ContentType.TEXT_HTML)
            }
        }
        server.start()
    }

    @Test
    void testHeadingForNewBlogPost() {
        def (title, author, category, content) =
            ['Bart was here', 'Bart', 'School', 'Cowabunga Dude!']
        def client = new WebClient()
        def page = client.getPage("${server.httpUrl}/postForm")
        assert 'Welcome to SimpBlog' == page.titleText
        def form = page.getFormByName('post')
        form.getInputByName('title').setValueAttribute("$title (Ersatz)")
        form.getSelectByName('author').getOptions().find{ it.text == author }.setSelected(true)
        form.getSelectByName('category').getOptions().find{ it.text == category }.setSelected(true)
        form.getTextAreaByName('content').setText(content)
        def result = form.getInputByName('btnPost').click()
        def m = result.getElementsByTagName('h1').item(0).textContent =~ /Post \d+: (.*) \([^)]*\)/
        assert m[0][1].contains(title)
        server.verify()
    }

    @After
    void shutdown() {
        server.stop()
    }
}
