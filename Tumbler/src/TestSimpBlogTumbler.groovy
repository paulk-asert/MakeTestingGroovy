//@Grab('pl.pragmatists.tumbler:tumbler:0.3.0')
import org.junit.runner.RunWith
import tumbler.*
import static tumbler.Tumbler.*

@RunWith(TumblerRunner)
@Story("New Blog Post")
class TestSimpBlogTumbler {
    TestSimpBlogTumbler() {
//        System.setProperty('generateReport', 'html')
        Narrative('''
            As a blog publisher
            In order to create some blog content
            I want a blogger to post a new blog
            So that I can keep my avid readers informed
            '''.trim())
    }

    def tester

    @Scenario
    void "creating a new blog entry"() {
        def title = 'Bart was here'
        def author = 'Bart'
        def category = 'School'
        def content = 'Cowabunga dude!'

        Given('we are on the New Blog Page')
            tester = new BlogTester('http://localhost:8080/postForm')
            tester.checkTitle 'Welcome to SimpBlog'

        When('we post a new blog')
            tester.postBlog title: title + ' (Spock)', category: category,
                    content: content, author: author

        Then('the new blog is posted')
            tester.checkHeadingMatches title + ' (Spock)'
            tester.checkSubheading 'Category', category
            tester.checkPostText content
            tester.checkSubheading 'Author', author
    }
}