//@Grab('org.quicktheories:quicktheories:0.14')
import groovy.transform.Field

import static org.quicktheories.quicktheories.QuickTheory.qt
import static org.quicktheories.quicktheories.generators.SourceDSL.*

def authors = ["Bart", "Homer", "Lisa", "Marge", "Maggie"]
def categories = ["Home", "Work", "Food", "Travel", "School"]
@Field suffix = ' (quicktheories)'

qt().withExamples(20).forAll(
        arbitrary().pick(authors),
        arbitrary().pick(categories),
        strings().basicLatinAlphabet().ofLengthBetween(0, 50 - suffix.size()),
        strings().basicLatinAlphabet().ofLengthBetween(0, 255)
).checkAssert { author, category, title, content ->
    postAndCheck author, category, title, content
}

def postAndCheck(String author, String category, String title, String content) {
    def tester = new BlogTester('http://localhost:8080/postForm')
    tester.postAndCheck title + suffix, category, author, content
}
