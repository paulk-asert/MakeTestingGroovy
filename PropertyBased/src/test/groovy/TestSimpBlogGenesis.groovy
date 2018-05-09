import spock.genesis.transform.Iterations
import spock.lang.Specification
import static spock.genesis.Gen.*

class TestSimpBlogGenesis extends Specification {
    static authors = ["Bart", "Homer", "Lisa", "Marge", "Maggie"]
    static categories = ["Home", "Work", "Food", "Travel", "School"]

    @Iterations(10)
    def 'the blog should be posted'() {
        given:
        def tester = new BlogTesterBoolean('http://localhost:8080/postForm')

        when:
        tester.postBlog title: title, category: category, content: content, author: author

        then:
        tester.checkAll title, category, author, content

        where:
        author << any(authors)
        category << integer(0..<categories.size()).map{ categories[it] }
        title << string(40).map{ it + ' (genesis)' }
        content << string(255)
    }
}
