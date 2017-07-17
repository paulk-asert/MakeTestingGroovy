class TestSimpBlogFluentApiMap extends BlogTestCase {

    void setUp() {
        Object.setUp()
        checkTitle 'Welcome to SimpBlog'
    }

    void testBartWasHere() {
        postBlog(
                title:    'Bart was here (HtmlUnit FluentApiMap)',
                category: 'School',
                content:  'Cowabunga Dude!',
                author:   'Bart'
        )

        checkHeadingMatches 'Post.*: Bart was here.*'
        checkSubheading 1, 'Category: School'
        checkSubheading 2, 'Author: Bart'
        checkPostText 'Cowabunga Dude!'
    }
}