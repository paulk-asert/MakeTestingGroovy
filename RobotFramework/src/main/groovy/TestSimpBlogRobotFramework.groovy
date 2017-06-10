//@Grab('org.robotframework:robotframework:3.0.2')
import org.robotframework.RobotFramework

int rc = RobotFramework.run("--outputdir", "build/robot-results", "src/blog_post.txt")
if (rc == 0) println "All tests passed"
else if (rc <= 250) println "$rc tests failed"
else println "Error occurred"

class PostBlogLibrary {
    private tester
    PostBlogLibrary(String url) { tester = new BlogTester(url) }

    void post(String author, String category, String title, String content) {
        tester.postAndCheck(title + ' (Robot Framework)', category, author, content)
    }

    void invalid(String author, String category, String title, String content) {
        tester.postBlog(title: title, category: category,
                author: author, content: content)
        assert tester.lastResult.body.textContent.contains('Creation failed! Validation Error?')
    }
}
