//@Grab('org.codehaus.gpars:gpars:0.12')
import groovyx.gpars.GParsPool

def testCases = [
    ['Title 1 (GPars)', 'Home',   'Bart',  'Content 1'],
    ['Title 2 (GPars)', 'Work',   'Homer', 'Content 2'],
    ['Title 3 (GPars)', 'Travel', 'Marge', 'Content 3'],
    ['Title 4 (GPars)', 'Food',   'Lisa',  'Content 4']
]

GParsPool.withPool {
    testCases.eachParallel{ title, category, author, content ->
            postAndCheck title, category, author, content
    }
}

def postAndCheck(String title, String category, String author, String content) {
    def tester = new BlogTester('http://localhost:8080/postForm')
    tester.postAndCheck title, category, author, content
}
