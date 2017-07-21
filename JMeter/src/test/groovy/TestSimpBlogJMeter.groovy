//import org.apache.jmeter.assertions.JSR223Assertion
import org.apache.jmeter.control.LoopController
import org.apache.jmeter.engine.StandardJMeterEngine
import org.apache.jmeter.protocol.http.control.gui.HttpTestSampleGui
import org.apache.jmeter.protocol.http.sampler.HTTPSampler
//import org.apache.jmeter.reporters.ResultCollector
//import org.apache.jmeter.reporters.Summariser
//import org.apache.jmeter.save.SaveService
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
httpSampler.setProperty(TestElement.TEST_CLASS, HTTPSampler.name)
httpSampler.setProperty(TestElement.GUI_CLASS, HttpTestSampleGui.name)

//def assertion = new JSR223Assertion(script: 'assert prev.getResponseDataAsString().matches(/(?sm).*<h1>Post \\d+: Bart was here \\(JMeter\\)<\\/h1>.*/)', scriptLanguage: 'groovy')

def loopController = new LoopController(loops: 4, first: true)
loopController.initialize()

def threadGroup = new ThreadGroup(numThreads: 4, rampUp: 1, samplerController: loopController)

def testPlan = new TestPlan("SimpBlog JMeter Groovy Test Plan")

def planTree = new HashTree()
planTree.add(testPlan)
def groupTree = planTree.add(testPlan, threadGroup)
groupTree.add(httpSampler)
// currently script is reset back to empty String during:
// StandardJMeterEngine.run->notifyTestListenersOfStart->TestBeanHelper.prepare
//groupTree.add(assertion)

//JMeterUtils.setJMeterHome('/path/to/jmeter')
//SaveService.saveTree(planTree, new FileOutputStream("hardcoded.jmx"))

// add Summariser output to get test progress in stdout:
// summary =     16 in 00:00:03 =    5.3/s Avg:   646 Min:    10 Max:  2901 Err:     0 (0.00%)
// Store execution results into a .jtl file
//def logger = new ResultCollector(new Summariser('summary'))
//logger.filename = "hardcoded.jtl"
//planTree.add(testPlan, logger)

jmeter.configure(planTree)
jmeter.run()
