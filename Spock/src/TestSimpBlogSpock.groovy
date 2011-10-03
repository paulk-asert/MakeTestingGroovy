//@Grab('org.spockframework:spock-core:0.5-groovy-1.8')
//@GrabExclude('org.codehaus.groovy:groovy-all')
import spock.lang.*

class TestSimpBlogSpock extends Specification {
    def tester

    @Unroll({"When $author posts a $category blog with content '$content' and title '$title' it should succeed"})
    def "when creating a new blog entry"() {
        given:
            tester = new BlogTesterBoolean('http://localhost:8080/postForm')
        and:
            tester.checkTitle 'Welcome to SimpBlog'

        when:
            tester.postBlog title: title, category: category, content: content, author: author

        then:
            tester.checkHeadingMatches title
            tester.checkSubheading 'Category', category
            tester.checkSubheading 'Author', author
        and:
            tester.checkPostText content

        where:
            title    << ['Title 1', 'Title 2', 'Title 3']*.plus(' (Spock)')
            author   << ['Bart', 'Homer', 'Lisa']
            category << ['Home', 'Work', 'Food']
            content  << ['Content A', 'Content B', 'Content C']
    }
}