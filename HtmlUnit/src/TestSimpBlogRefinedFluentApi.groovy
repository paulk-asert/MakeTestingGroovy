class TestSimpBlogRefinedFluentApi extends GroovyTestCase {

    private tester = new BlogTester('http://localhost:8080/postForm')

    void testBartWasHere() {
        tester.checkTitle 'Welcome to SimpBlog'

        tester.postBlog title: 'Bart was here (HtmlUnit FluentApi)',
                category: 'Home', content: 'Cowabunga Dude!', author: 'Bart'

        tester.checkHeadingMatches 'Post.*: Bart was here.*'
        tester.checkSubheading 'Category', 'Home'
        tester.checkSubheading 'Author','Bart'
        tester.checkPostText 'Cowabunga Dude!'
    }
}