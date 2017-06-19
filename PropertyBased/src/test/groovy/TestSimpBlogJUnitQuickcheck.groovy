//@Grab('com.pholser:junit-quickcheck-generators:0.7')
//@Grab('com.pholser:junit-quickcheck-core:0.7')
import com.pholser.junit.quickcheck.Property
import com.pholser.junit.quickcheck.When
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck
import org.junit.runner.RunWith

import static org.junit.Assume.assumeTrue

@RunWith(JUnitQuickcheck)
class TestSimpBlogJUnitQuickcheck {
    //enum authors { Bart, Homer, Lisa, Marge, Maggie }
    private authors = ["Bart", "Homer", "Lisa", "Marge", "Maggie"]
    private categories = ["Home", "Work", "Food", "Travel", "School"]
    private tester = new BlogTester('http://localhost:8080/postForm')
    private String suffix = ' (junit quickcheck)'

    @Property(trials = 50)
    void shouldHold(@When(satisfies='#_.length() < 255', discardRatio=200) String content,
                    String title,
                    @When(satisfies='#_ >= 0 && #_ < 5', discardRatio=200) byte authorIndex,
                    byte categoryIndex) {
        assumeTrue title.size() <= 50 - suffix.size()
        tester.postAndCheck title + suffix, categories[categoryIndex % categories.size()], authors[authorIndex], content
    }
}
