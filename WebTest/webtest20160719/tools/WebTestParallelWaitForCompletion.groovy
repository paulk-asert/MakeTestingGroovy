/**
 * Experimental feature: allows to simply run WebTests in parallel
 * @author Marc Guillemot
 */
def queue = project.references["wt-queue"]

// remove marker, now threads should finish if they have nothing to do
synchronized (queue)
{
	queue.remove Object.class
}

def workers = project.references["wt-workers"]
synchronized (queue) {
	while (workers)	{
		queue.wait(5000)
	}
}
