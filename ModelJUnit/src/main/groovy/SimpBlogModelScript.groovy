//@Grab('nz.ac.waikato.modeljunit:modeljunit:jar:2.5')
import nz.ac.waikato.modeljunit.coverage.*
import nz.ac.waikato.modeljunit.*

class SimpBlogModel implements FsmModel {
    boolean authorSelected = false
    boolean categorySelected = false
    boolean titleEntered = false
    boolean contentEntered = false
    int count = 0
    def client, page, form

    // Example states: __ __ __ __, AU __ __ __, AU CA TI CO
    def getState() {
        "${authorSelected ? ' AU ' : ' __ '}${categorySelected ? ' CA ' : ' __ '}" +
        "${titleEntered ? ' TI ' : ' __ '}${contentEntered ? ' CO ' : ' __ '}"
    }

    void reset(boolean testing) {
        authorSelected = false
        categorySelected = false
        titleEntered = false
        contentEntered = false
    }

    boolean "enter title Guard"() {!titleEntered}
    @Action void "enter title "() {
        titleEntered = true
    }

    boolean "enter content Guard"() {!contentEntered}
    @Action void "enter content "() {
        contentEntered = true
    }

    boolean "choose author Guard"() {!authorSelected}
    @Action void "choose author "() {
        authorSelected = true
    }

    boolean "pick category Guard"() {!categorySelected}
    @Action void "pick category "() {
        categorySelected = true
    }

    boolean "submit post Guard"() {categorySelected && authorSelected &&
            titleEntered && contentEntered}
    @Action void "submit post "() {
        def tester = new BlogTester('http://localhost:8080/postForm')
        tester.postAndCheck 'Bart was here (ModelJUnit)', 'Home', 'Bart', 'Cowabunga Dude!'
        reset(true)
    }
}

def tester = new RandomTester(new SimpBlogModel())
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
tester.generate 50

println '\nMetrics Summary:'
tester.printCoverage()

def graphListener = tester.model.getListener("graph")
graphListener.printGraphDot "simpblog.dot"
println "\nGraph contains " + graphListener.graph.numVertices() +
        " states and " + graphListener.graph.numEdges() + " transitions."
