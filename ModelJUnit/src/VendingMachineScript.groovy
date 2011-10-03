// require modeljunit.jar
import nz.ac.waikato.modeljunit.coverage.*
import nz.ac.waikato.modeljunit.*

class VendingMachineModel implements FsmModel {
    def state = 0 // 0,25,50,75,100

    void reset(boolean testing) {state = 0}

    boolean vendGuard() {state == 100}
    @Action void vend() {state = 0}

    boolean coin25Guard() {state <= 75}
    @Action void coin25() {state += 25}

    boolean coin50Guard() {state <= 50}
    @Action void coin50() {state += 50}
}

def tester = new RandomTester(new VendingMachineModel())
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
tester.generate 20

println '\nMetrics Summary:'
tester.printCoverage()

def graphListener = tester.model.getListener("graph")
graphListener.printGraphDot "vending.dot"
println "\nGraph contains " + graphListener.graph.numVertices() +
        " states and " + graphListener.graph.numEdges() + " transitions."
