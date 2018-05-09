class TestSimpBlogGUnit extends GroovyTestCase {
    def tester

    void setUp() {
        tester = new BlogTester('http://localhost:8080/postForm')
    }

    void testBartWasHere() {
        def title = 'Bart was here (HtmlUnitDslBasic GUnit)'
        def category = 'School'
        def author = 'Bart'
        def content = 'Cowabunga Dude!'
        tester.postAndCheck title, category, author, content
    }
}
