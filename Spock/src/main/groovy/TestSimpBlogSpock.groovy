import spock.lang.*

class TestSimpBlogSpock extends Specification {
    def tester

    def "Bart creates a new blog entry"() {
        given:
            tester = new BlogTesterBoolean('http://localhost:8080/postForm')
        and:
            tester.checkTitle 'Welcome to SimpBlog'

        when:
            tester.postBlog title: 'Bart was here (Spock)', category: 'Home',
                    content: 'Cowabunga Dude!', author: 'Bart'

        then:
            tester.checkHeadingMatches 'Bart was here (Spock)'
            tester.checkSubheading 'Category', 'Home'
        and:
            tester.checkPostText 'Cowabunga Dude!'
            tester.checkSubheading 'Author', 'Bart'
    }
}