//@Grab('org.spockframework:spock-core:1.1-groovy-2.4')
//@GrabExclude('org.codehaus.groovy:groovy-all')
import spock.lang.*

class TestSimpBlogSpockDD extends Specification {
    def tester

    @Unroll("When #author posts a #category blog with content '#content' and title '#title' it should succeed")
    def "when creating a new blog entry"() {
        given:
            tester = new BlogTesterBoolean('http://localhost:8080/postForm')
        and:
            tester.checkTitle 'Welcome to SimpBlog'

        when:
            tester.postBlog title: title + ' (Spock)', category: category,
                    content: content, author: author

        then:
            tester.checkHeadingMatches title + ' (Spock)'
            tester.checkSubheading 'Category', category
        and:
            tester.checkPostText content
            tester.checkSubheading 'Author', author

        where:
            title    << ['Title 1', 'Title 2', 'Title 3']
            author   << ['Bart', 'Homer', 'Lisa']
            category << ['Home', 'Work', 'Food']
            content  << ['Content A', 'Content B', 'Content C']
    }
}