//@Grab('org.spockframework:spock-core:1.1-groovy-2.4')
//@Grab('cglib:cglib-nodep:3.2.5')
//@Grab('org.objenesis:objenesis:2.5.1')
//@GrabExclude('org.codehaus.groovy:groovy-all')
import org.w3c.dom.Document
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import spock.lang.Specification

class TestSimpBlogMockSpock extends Specification {
    def "Bart creates a new blog entry"() {
        given:
        def tester = new BlogTesterBoolean('http://localhost:8080/postForm')
        def (node, nodeList, page) = [Mock(Node), Mock(NodeList), Mock(Document)]
        and:
        tester.lastResult = page

        when:
        def matched = tester.checkHeadingMatches 'Bart was here (Mock Spock)'

        then:
        1 * page.getElementsByTagName('h1') >> nodeList
        1 * nodeList.item(0) >> node
        1 * node.getTextContent() >> 'Post 99: Bart was here (Mock Spock)'
        matched
    }
}