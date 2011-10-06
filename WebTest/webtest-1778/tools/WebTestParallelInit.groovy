/**
 * Experimental feature: allows to simply run WebTests in parallel
 * @author Marc Guillemot
 */
import com.canoo.webtest.ant.WebtestTask
import com.canoo.webtest.engine.Configuration
import org.apache.log4j.Logger;

public class WebtestTaskParallel extends WebtestTask
{
	private static final Logger LOG = Logger.getLogger(WebtestTaskParallel)
	static queue = [Object.class]
	static webtestMonitor
	static counter = 1

	public void execute() 
	{
		LOG.debug "Adding test to the queue: ${this.getName()}"
		synchronized (queue)
		{
			getConfig().resultFolderIndex = counter++
			queue.add(queue.size() - 1, this)
			queue.notifyAll()
	
			// if WebTestMonitor, then notify webtest added
			if (webtestMonitor)
			{
				webtestMonitor.notifyWebTestAdded(this)
			}
		}
		return
	}

	public void executeReally()
	{
		println "[T: ${Thread.currentThread().name}]-- executeReally"
		synchronized (queue)
		{
			if (webtestMonitor)
			{
				webtestMonitor.notifyWebTestStarted(this)
			}
		}
		try
		{
			super.execute()
		}
		catch (e)
		{
			println "[T: ${Thread.currentThread().name}]-- catched $e"
			e.printStackTrace()
		}
		println "[T: ${Thread.currentThread().name}]-- executed"

		synchronized (queue)
		{
			if (webtestMonitor)
			{
				def rootResult = getResultBuilderListener().rootResult
				def failed = rootResult.error || rootResult.failure
				webtestMonitor.notifyWebTestFinished(this, !failed)
			}
		}
	}
}
println "Changing webtest task"
project.addTaskDefinition("webtest", WebtestTaskParallel)

// get the WebTestMonitor if any
WebtestTaskParallel.webtestMonitor = project.getReference("wt.WebTestMonitor.ref")

// the queue and the workers
def queue = WebtestTaskParallel.queue
project.references["wt-queue"] = queue 
	
workers = []
project.references["wt-workers"] = workers 
	
def c = { self ->
	def webtest
	while (true)
	{
		synchronized (queue)
		{
			if (queue.isEmpty())
			{
				workers.remove(self)
				println "[T: ${Thread.currentThread().name}]-- ${workers.size()} workers after removing myself"
				queue.notifyAll()
				println "[T: ${Thread.currentThread().name}]-- no job for me anymore. Exiting"
				return
			}
			else
			{
				if (queue[0].is(Object.class))
				{
					// just the marker, not test for me now
					println "[T: ${Thread.currentThread().name}]-- nothing currently in queue (${queue.size()}) for me. Waiting"
					queue.wait()
				}
				else
				{
					println "[T: ${Thread.currentThread().name}]-- picking webtest"
					webtest = queue.remove(0)
				}
			}
		}
		if (webtest)
		{
			println "[T: ${Thread.currentThread().name}]-- Starting ${webtest.getName()} - ${webtest.getConfig().resultpath}"
			webtest.executeReally()
			println "[T: ${Thread.currentThread().name}]-- Finished ${webtest.getName()} - ${webtest.getConfig().resultpath}"
			webtest = null
		}
	}
}


println project.properties["wt.parallel.nbWorkers"]
def nbWorkers = Integer.parseInt(project.properties["wt.parallel.nbWorkers"])
nbWorkers.times() {
	def worker = c.clone()
	synchronized (queue)
	{
		workers.add worker
	}
	Thread.start worker.curry(worker)
}

