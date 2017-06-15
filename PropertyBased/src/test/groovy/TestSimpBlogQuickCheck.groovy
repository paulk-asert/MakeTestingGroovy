//@Grab('net.java:quickcheck:0.6')
import static net.java.quickcheck.generator.PrimitiveGeneratorSamples.*

def authors = ["Bart", "Homer", "Lisa", "Marge", "Maggie"]
def categories = ["Home", "Work", "Food", "Travel", "School"]

// we haven't limited title to 50 chars but ok with limit of 10
10.times {
    postAndCheck anyString(), anyFixedValue(categories), anyFixedValue(authors), anyString()
}

def postAndCheck(String title, String category, String author, String content) {
    def tester = new BlogTester('http://localhost:8080/postForm')
    tester.postAndCheck title + ' (quickcheck)', category, author, content
}
