import org.apache.jmeter.assertions.JSR223Assertion
import org.apache.jmeter.control.LoopController
import org.apache.jmeter.engine.StandardJMeterEngine
import org.apache.jmeter.protocol.http.sampler.HTTPSampler
import org.apache.jmeter.testelement.TestPlan
import org.apache.jmeter.threads.ThreadGroup
import org.apache.jmeter.util.JMeterUtils
import org.apache.jorphan.collections.HashTree

def jmeter = new StandardJMeterEngine()
JMeterUtils.loadJMeterProperties(getClass().classLoader.getResource('jmeter.properties').file)
JMeterUtils.initLocale()

//def params = [
//        domain: 'localhost', port: 8088,
//        path: '/', method: 'GET'
//]
def params = [
        domain: 'localhost', port: 8080,
        path: '/addPost', method: 'POST',
        useKeepAlive: true, followRedirects: true
]
def httpSampler = new HTTPSampler(params)
httpSampler.addArgument('author', '1')
httpSampler.addArgument('category', '1')
httpSampler.addEncodedArgument('title', 'Bart was here (JMeter hand-coded)')
httpSampler.addEncodedArgument('content', 'Cowabunga dude!')

def assertion = new JSR223Assertion(script: 'println new Date()', scriptLanguage: 'groovy')

def loopController = new LoopController(loops: 1/*, first: true*/)
loopController.addTestElement(httpSampler)
loopController.setFirst(true)
loopController.addTestElement(assertion)
loopController.initialize()

def threadGroup = new ThreadGroup(numThreads: 1, rampUp: 1, /*duration: 10000, */samplerController: loopController)
//threadGroup.setProperty("ThreadGroup.on_sample_error", "continue")

def testPlan = new TestPlan("SimpBlog JMeter Groovy Test Plan")
//testPlan.addThreadGroup(threadGroup)

def testPlanTree = new HashTree()
testPlanTree.add("testPlan", testPlan)
testPlanTree.add("loopController", loopController)
testPlanTree.add("threadGroup", threadGroup)
testPlanTree.add("httpSampler", httpSampler)

jmeter.configure(testPlanTree)
jmeter.run()
