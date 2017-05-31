import org.junit.*
import org.junit.runners.Parameterized
import org.junit.runner.RunWith
import org.junit.runners.Parameterized.Parameters

@RunWith(Parameterized)
class TestSimpBlogJUnit4DD {
    def tester
    def author, title, category, content

    TestSimpBlogJUnit4DD(author, title, category, content) {
        this.author = author
        this.title = title
        this.category = category
        this.content = content
    }

    @Parameters static data() {
        return [
            ['Bart', 'Title 1', 'Home', 'Content 1'],
            ['Homer', 'Title 2', 'Work', 'Content 2'],
            ['Marge', 'Title 3', 'Food', 'Content 3']
        ].collect{ it as String[] }
    }

    @Before
    void setUp() {
        tester = new BlogTester('http://localhost:8080/postForm')
    }

    @Test
    void newBlogPost() {
        tester.checkTitle 'Welcome to SimpBlog'
        def fullTitle = title + ' (JUnit4 Data Driven)'
        tester.postBlog title: fullTitle,
                category: category, content: content, author: author

        tester.checkHeadingMatches fullTitle
        tester.checkSubheading 'Category', category
        tester.checkSubheading 'Author', author
        tester.checkPostText content
    }
}
