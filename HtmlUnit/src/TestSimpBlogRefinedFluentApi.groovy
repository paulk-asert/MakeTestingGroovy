class TestSimpBlogRefinedFluentApi extends GroovyTestCase {

    private tester = new BlogTester('http://localhost:8080/postForm')

    void testBartWasHere() {
        tester.checkTitle 'Welcome to SimpBlog'

        tester.postBlog title: 'Bart was here (HtmlUnit FluentApi)',
                category: 'School', content: 'Cowabunga Dude!', author: 'Bart'

        tester.checkHeadingMatches 'Post.*: Bart was here.*'
        tester.checkSubheading 'Category', 'School'
        tester.checkSubheading 'Author','Bart'
        tester.checkPostText 'Cowabunga Dude!'
    }

    void testBartWasAlsoHere() {
        tester.postAndCheck 'Bart was also here (HtmlUnit FluentApi)', 'School', 'Bart', 'Cowabunga Dude!'
    }
}