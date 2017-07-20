import org.apache.jmeter.assertions.JSR223Assertion
import org.apache.jmeter.control.LoopController
import org.apache.jmeter.engine.StandardJMeterEngine
import org.apache.jmeter.protocol.http.sampler.HTTPSampler
import org.apache.jmeter.testbeans.gui.TestBeanGUI
import org.apache.jmeter.testelement.TestElement
import org.apache.jmeter.testelement.TestPlan
import org.apache.jmeter.threads.ThreadGroup
import org.apache.jmeter.util.JMeterUtils
import org.apache.jorphan.collections.HashTree

def jmeter = new StandardJMeterEngine()
JMeterUtils.loadJMeterProperties(getClass().classLoader.getResource('jmeter.properties').file)
JMeterUtils.initLocale()

def params = [
        domain: 'localhost', port: 8080,
        path: '/addPost', method: 'POST',
]
def httpSampler = new HTTPSampler(params)
httpSampler.addArgument('author', '1')
httpSampler.addArgument('category', '1')
httpSampler.addEncodedArgument('title', 'Bart was here (JMeter hand-coded)')
httpSampler.addEncodedArgument('content', 'Cowabunga dude!')

def assertion = new JSR223Assertion(script: 'assert prev.getResponseDataAsString().matches(/(?sm).*<h1>Post \\d+: Bart was here \\(JMeter\\)<\\/h1>.*/)', scriptLanguage: 'groovy')

def loopController = new LoopController(loops: 2, first: true)
loopController.addTestElement(httpSampler)
loopController.addTestElement(assertion)
loopController.initialize()

def threadGroup = new ThreadGroup(numThreads: 2, rampUp: 1, samplerController: loopController)

def testPlan = new TestPlan("SimpBlog JMeter Groovy Test Plan")

def planTree = new HashTree()
planTree.add(testPlan)
def groupTree = planTree.add(testPlan, threadGroup)
groupTree.add(httpSampler)
//groupTree.add(assertion)

jmeter.configure(planTree)
jmeter.run()
