@Grab('org.robotframework:robotframework:2.6.3')
import org.robotframework.RobotFramework

int rc = RobotFramework.run("--outputdir", "../robot-results", "blog_post.txt")
if (rc == 0) println "All tests passed"
else if (rc <= 250) println "$rc tests failed"
else println "Error occurred"

class PostBlogLibrary {
    public final static String ROBOT_LIBRARY_VERSION = "1.0"
    private tester
    PostBlogLibrary(String url) { tester = new BlogTester(url) }

    void post(String author, String category, String title, String content) {
        tester.postAndCheck(title + ' (Robot Framework)', category, author, content)
    }
}