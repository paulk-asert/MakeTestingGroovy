//@Grab('org.seleniumhq.selenium:htmlunit-driver:2.27')
//@Grab('org.seleniumhq.selenium:selenium-support:3.4.0')
//@GrabExclude('xml-apis:xml-apis')
import org.openqa.selenium.By
//import org.openqa.selenium.chrome.ChromeDriver
//import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.htmlunit.HtmlUnitDriver
//import org.openqa.selenium.ie.InternetExplorerDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.PageFactory
import org.openqa.selenium.WebDriver

// uncomment one driver from below
//def driver = new ChromeDriver()
//def driver = new FirefoxDriver()
def driver = new HtmlUnitDriver()
//def driver = new InternetExplorerDriver()

driver.get('http://localhost:8080/postForm')
assert driver.title == 'Welcome to SimpBlog'

def newBlogPage = new NewPostPage(driver)
newBlogPage.title('Bart was here (WebDriver w/ pages)')
newBlogPage.author('Bart')
newBlogPage.category('School')
newBlogPage.content('Cowabunga dude!')
def viewBlogPage = newBlogPage.post() // could get this to return a ViewPostPage

assert viewBlogPage.mainHeading() ==~ 'Post.*: Bart was here.*'
assert viewBlogPage.categoryHeading() == 'Category: School'
assert viewBlogPage.authorHeading() == 'Author: Bart'
assert viewBlogPage.blogText() == 'Cowabunga dude!'

driver.close()

class NewPostPage {
    private driver
    NewPostPage(WebDriver driver) {
        this.driver = driver
        PageFactory.initElements(driver, this)
    }
    private WebElement title, category, author, content, btnPost
    def title(title) { this.title.sendKeys(title) }
    def category(category) { this.category.findElements(By.tagName("option")).find{ it.text == category }.click() }
    def author(author) { this.author.findElements(By.tagName("option")).find{ it.text == author }.click() }
    def content(content) { this.content.sendKeys(content) }
    ViewPostPage post() { btnPost.click(); sleep 100; new ViewPostPage(driver) }
}

class ViewPostPage {
    private driver
    ViewPostPage(WebDriver driver) { this.driver = driver }
    def mainHeading() { driver.findElement(By.tagName("h1")).text }
    def categoryHeading() { driver.findElements(By.tagName("h3"))[1].text }
    def authorHeading() { driver.findElements(By.tagName("h3"))[2].text }
    def blogText() {
        def row = driver.findElement(By.xpath("//table/tbody/tr"))
        def col = row.findElement(By.tagName("td"))
        def para = col.findElement(By.tagName("p"))
        para.text
    }
}
