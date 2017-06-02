//@Grab('nz.ac.waikato.modeljunit:modeljunit:jar:2.5')
import nz.ac.waikato.modeljunit.coverage.*
import nz.ac.waikato.modeljunit.*

class SimpBlogModel2 implements FsmModel {
    static final URL = 'http://localhost:8080/postForm'
    static final CATEGORIES = ['Home', 'Work', 'Travel', 'Food', 'School']
    static final BLOGGERS = ['Bart', 'Homer', 'Lisa', 'Marge', 'Maggie']
    static final EMPTY = ' __ '
    String author = ''
    String category = ''
    String title = ''
    String content = ''
    int count = 0
    def client, page, form

    def getState() {
        "${author ? / AU=$author / : EMPTY}${category ? / CA=$category / : EMPTY}" +
        "${title ? ' TI ' : EMPTY}${content ? ' CO ' : EMPTY}"
    }

    void reset(boolean testing) {
        author = ''
        category = ''
        title = ''
        content = ''
    }

    boolean enterTitleGuard() {!title}
    @Action void enterTitle() {
        title = "Title ${count++}"
    }

    boolean enterContentGuard() {!content}
    @Action void enterContent() {
        content = "Content ${count++}"
    }

    boolean chooseAuthorGuard() { !author }
    @Action void chooseAuthor() {
        author = BLOGGERS[new Random().nextInt(BLOGGERS.size())]
    }

    boolean pickCategoryGuard() { !category }
    @Action void pickCategory() {
        category = CATEGORIES[new Random().nextInt(CATEGORIES.size())]
    }

    boolean submitPostGuard() { category && author && title && content }
    @Action void submitPost() {
        def tester = new BlogTester('http://localhost:8080/postForm')
        tester.postAndCheck title, category, author, content
        reset(true)
    }
}

def tester = new GreedyTester(new SimpBlogModel2())
tester.buildGraph()
def metrics = [
    new ActionCoverage(),
    new StateCoverage(),
    new TransitionCoverage(),
    new TransitionPairCoverage()
]
metrics.each {
    tester.addCoverageMetric it
}

tester.addListener "verbose"
tester.generate 200

println '\nMetrics Summary:'
tester.printCoverage()

def graphListener = tester.model.getListener("graph")
graphListener.printGraphDot "simpblog_adv.dot"
println "\nGraph contains " + graphListener.graph.numVertices() +
        " states and " + graphListener.graph.numEdges() + " transitions."
