import org.jboss.arquillian.container.test.api.Deployment
import org.jboss.arquillian.drone.api.annotation.Drone
import org.jboss.arquillian.junit.Arquillian
//import org.jboss.arquillian.test.api.ArquillianResource
import org.jboss.shrinkwrap.api.ShrinkWrap
import org.jboss.shrinkwrap.api.spec.WebArchive
import org.junit.Test
import org.junit.runner.RunWith
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
//import simpblog.Author
//import simpblog.Bootstrap
//import simpblog.Category
//import simpblog.MyServlet
//import simpblog.Post

@RunWith(Arquillian)
class TestSimpBlogArquillian {
    @Deployment(testable = false)
    static WebArchive createDeployment() {
        return ShrinkWrap
                .create(WebArchive, "blog.war")
//                .addClasses(Author, Post, Category, MyServlet, Bootstrap)
                .setWebXML(new File("src/test/webapp/WEB-INF/web.xml"))
    }

    @Drone
    private WebDriver browser

//    @ArquillianResource
//    private URL deploymentURL
    private URL deploymentURL = new URL('http://localhost:8080/')

    @Test
    void "should create and display blog"() {
        browser.get(deploymentURL.toExternalForm() + 'postForm')

        browser.findElement(By.name('title')).sendKeys('Bart was here (Arquillian)')
        def select = browser.findElement(By.name('category'))
        select.findElements(By.tagName("option")).find{ it.text == 'School' }.click()
        browser.findElement(By.name('content')).sendKeys('Cowabunga dude!')
        browser.findElement(By.name('btnPost')).click()

        sleep 100

        assert browser.findElement(By.tagName("h1")).text.matches('Post.*: Bart was here.*')
        def h3headers = browser.findElements(By.tagName("h3"))
        assert h3headers[1].text == 'Category: School'
        assert h3headers[2].text == 'Author: Bart'

        // try a more advanced finder
        // content is at: //TABLE/TBODY/TR/TD/P
        def row = browser.findElement(By.xpath("//table/tbody/tr"))
        def col = row.findElement(By.tagName("td"))
        def para = col.findElement(By.tagName("p"))
        assert para.text == 'Cowabunga dude!'
    }
}
