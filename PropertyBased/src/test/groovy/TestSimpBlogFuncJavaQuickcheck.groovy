//@Grab('org.functionaljava:functionaljava-quickcheck:4.7')
import fj.F4
import fj.test.Property
import fj.test.reflect.CheckParams
import fj.test.runner.PropertyTestRunner
import org.junit.runner.RunWith

import static fj.test.Arbitrary.arbAlphaNumString
import static fj.test.Arbitrary.arbInteger
import static fj.test.Property.prop
import static fj.test.Property.property

@RunWith(PropertyTestRunner.class)
@CheckParams(maxSize = 35)
class TestSimpBlogFuncJavaQuickcheck {
    def authors = ["Bart", "Homer", "Lisa", "Marge", "Maggie"]
    def categories = ["Home", "Work", "Food", "Travel", "School"]
    def suffix = ' (fj quickcheck)'

    Property testBlog() {
        return property(
                arbInteger,
                arbInteger,
                arbAlphaNumString,
                arbAlphaNumString,
                { authorIndex, categoryIndex, title, content ->
                    def author = authors[authorIndex % authors.size()]
                    def category = categories[categoryIndex % categories.size()]
                    prop(postAndCheck(author, category, title, content))
                } as F4)
    }

    private boolean postAndCheck(String author, String category, String title, String content) {
        def tester = new BlogTesterBoolean('http://localhost:8080/postForm')
        tester.postBlog title: title + suffix, category: category, content: content, author: author
        tester.checkAll title + suffix, category, author, content
    }
}