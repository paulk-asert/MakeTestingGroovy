class TestSimpBlogDsl extends BlogTestCase {

    private the, has, a, with, heading
    void setUp() {
        super.setUp()
    }

    void testBartWasHere() {
        check the browser has title 'Welcome to SimpBlog'
        post a blog with title 'Bart was here (HtmlUnit DSL)' \
            and with author 'Bart' \
            and with category 'School' \
            and with content 'Cowabunga Dude!'
        check the main heading matches 'Post.*: Bart was here.*'
        check the category has value 'School'
        check the author has value 'Bart'
        check the blog has text 'Cowabunga Dude!'
    }

    def post(_a) {
        [
                blog: { _with ->
                    [title: { postTitle ->
                        [and: { __with ->
                            [author: { postAuthor ->
                                [and: { ___with ->
                                    [category: { postCategory ->
                                        [and: { ____with ->
                                            [content: { postContent ->
                                                postBlog(title: postTitle,
                                                        author:postAuthor,
                                                        content:postContent,
                                                        category: postCategory)
                }   ]}  ]}  ]}  ]}  ]}  ]}  ]}
        ]
    }

    def check(_the) {
        [
                browser: { _has -> [title: { checkTitle it }]},
                main: { _heading -> [matches: { checkHeadingMatches it }]},
                category: { _has -> [value: { checkSubheading 1, "Category: $it" }]},
                author: { _has -> [value: { checkSubheading 2, "Author: $it" }]},
                blog: { _has -> [text: { checkPostText it }]}
        ]
    }
}