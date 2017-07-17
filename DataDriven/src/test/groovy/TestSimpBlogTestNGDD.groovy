//@Grab('org.testng:testng:6.11')
//@GrabExclude('junit:junit')
import org.testng.annotations.*

class TestSimpBlogTestNGDD {
    def tester

    @BeforeClass
    void setUp() {
        tester = new BlogTester('http://localhost:8080/postForm')
    }

    @DataProvider(name='SimpBlogDataProvider')
    Object[][] data() {
        return [
            ['Bart', 'Title 1', 'Home', 'Content 1'],
            ['Homer', 'Title 2', 'Work', 'Content 2'],
            ['Marge', 'Title 3', 'Food', 'Content 3']
        ].collect{ it as Object[] } as Object[]
    }

    @Test(dataProvider = "SimpBlogDataProvider")
    void newBlogPost(author, title, category, content) {
        tester.checkTitle 'Welcome to SimpBlog'

        def fullTitle = title + ' (TestNG Data Driven)'
        tester.postBlog title: fullTitle,
                category: category, content: content, author: author

        tester.checkHeadingMatches fullTitle
        tester.checkSubheading 'Category', category
        tester.checkSubheading 'Author', author
        tester.checkPostText content
   }
}