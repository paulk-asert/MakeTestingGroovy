class TestSimpBlogFluentApi extends BlogTestCase {

    void setUp() {
        super.setUp()
        checkTitle('Welcome to SimpBlog')
    }

    void testBartWasHere() {
        prepareBlog()
            .withTitle('Bart was here (HtmlUnit FluentApi)')
            .withAuthor('Bart')
            .withCategory('Home')
            .withContent('Cowabunga Dude!')
            .post()

        checkHeadingMatches 'Post.*: Bart was here.*'
        checkSubheading 1, 'Category: Home'
        checkSubheading 2, 'Author: Bart'
        checkPostText 'Cowabunga Dude!'
    }
}