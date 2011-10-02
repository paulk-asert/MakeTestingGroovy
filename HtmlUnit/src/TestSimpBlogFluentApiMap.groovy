class TestSimpBlogFluentApiMap extends BlogTestCase {

    void setUp() {
        super.setUp()
        checkTitle 'Welcome to SimpBlog'
    }

    void testBartWasHere() {
        postBlog(
                title:    'Bart was here (HtmlUnit FluentApiMap)',
                category: 'Home',
                content:  'Cowabunga Dude!',
                author:   'Bart'
        )

        checkHeadingMatches 'Post.*: Bart was here.*'
        checkSubheading 1, 'Category: Home'
        checkSubheading 2, 'Author: Bart'
        checkPostText 'Cowabunga Dude!'
    }
}