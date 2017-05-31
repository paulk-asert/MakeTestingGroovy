import static Placeholder.*
enum Placeholder { the, has, a, with, heading }

class TestSimpBlogDslStaticTypes extends BlogTestCase {

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

    def check(Placeholder the) {
        new CheckHolder(this)
    }

    def post(Placeholder a) {
        new PostHolder(this)
    }
}

class PostHolder {
    private fixture
    PostHolder(fixture) { this.fixture = fixture }
    def blog(Placeholder with) { new PostDetailsHolder(fixture) }
}

class PostDetailsHolder {
    private fixture
    private title, author, category, content
    PostDetailsHolder(fixture) { this.fixture = fixture }
    def title(String text) { title = text; this }
    def author(String text) { author = text; this }
    def category(String text) { category = text; this }
    def content(String text) {
        content = text
        fixture.postBlog(title: title, author: author,
                content: content, category: category)
    }
    def and(Placeholder with) { this }
}

class CheckHolder {
    private fixture
    CheckHolder(fixture) { this.fixture = fixture }
    def main(Placeholder heading) { new HeadingHolder(fixture) }
    def browser(Placeholder has) { new BrowserHolder(fixture) }
    def category(Placeholder has) { new SubheadingHolder(fixture, 'Category', 1) }
    def author(Placeholder has) { new SubheadingHolder(fixture, 'Author', 2) }
    def blog(Placeholder has) { new BlogHolder(fixture) }
}

class HeadingHolder {
    private fixture
    HeadingHolder(fixture) { this.fixture = fixture }
    def matches(String text) { fixture.checkHeadingMatches text }
}

class BrowserHolder {
    private fixture
    BrowserHolder(fixture) { this.fixture = fixture }
    def title(String text) { fixture.checkTitle text }
}

class BlogHolder {
    private fixture
    BlogHolder(fixture) { this.fixture = fixture }
    def text(String text) { fixture.checkPostText text }
}

class SubheadingHolder {
    private fixture
    private type
    private headingNum
    SubheadingHolder(fixture, type, num) { this.fixture = fixture; this.type = type; headingNum = num }
    def value(String text) { fixture.checkSubheading headingNum, "$type: $text" }
}
