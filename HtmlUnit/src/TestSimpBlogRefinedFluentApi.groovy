class TestSimpBlogRefinedFluentApi extends GroovyTestCase {

    private tester = new BlogTester('http://localhost:8080/postForm')

    void testBartWasHere() {
        tester.checkTitle 'Welcome to SimpBlog'
        def title = 'Bart was here (HtmlUnit FluentApi)'
        tester.postBlog title: title,
                category: 'School', content: 'Cowabunga Dude!', author: 'Bart'

        tester.checkHeadingMatches title
        tester.checkSubheading 'Category', 'School'
        tester.checkSubheading 'Author','Bart'
        tester.checkPostText 'Cowabunga Dude!'
    }

    void testBartWasAlsoHere() {
        tester.postAndCheck 'Bart was also here (HtmlUnit FluentApi)', 'School', 'Bart', 'Cowabunga Dude!'
    }
}