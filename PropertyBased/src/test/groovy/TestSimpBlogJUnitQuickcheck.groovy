//@Grab('com.pholser:junit-quickcheck-generators:0.7')
//@Grab('com.pholser:junit-quickcheck-core:0.7')
import com.pholser.junit.quickcheck.From
import com.pholser.junit.quickcheck.Property
import com.pholser.junit.quickcheck.When
import com.pholser.junit.quickcheck.generator.GenerationStatus
import com.pholser.junit.quickcheck.generator.Generator
import com.pholser.junit.quickcheck.generator.InRange
import com.pholser.junit.quickcheck.random.SourceOfRandomness
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
                    @From(NonNegativeInts) int authorIndex,
                    @InRange(minInt=0, maxInt=4) int categoryIndex) {
        assumeTrue title.size() <= 50 - suffix.size()
        tester.postAndCheck title + suffix, categories[categoryIndex], authors[authorIndex % authors.size()], content
    }

    static class NonNegativeInts extends Generator<Integer> {
        NonNegativeInts() { super([Integer, int]) }
        @Override Integer generate(SourceOfRandomness random, GenerationStatus status) {
            Math.abs(random.nextInt())
        }
    }
}
