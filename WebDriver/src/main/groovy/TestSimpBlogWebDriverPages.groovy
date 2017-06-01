//@Grab('org.seleniumhq.selenium:selenium-htmlunit-driver:2.52.0')
//@Grab('org.seleniumhq.selenium:selenium-support:2.52.0')
//@GrabExclude('xml-apis:xml-apis')
import org.openqa.selenium.By
import org.openqa.selenium.htmlunit.HtmlUnitDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.PageFactory
import org.openqa.selenium.WebDriver

def driver = new HtmlUnitDriver()
driver.get('http://localhost:8080/postForm')
assert driver.title == 'Welcome to SimpBlog'

def newBlogPage = PageFactory.initElements(driver, NewPostPage)
newBlogPage.title('Bart was here (WebDriver)')
newBlogPage.author('Bart')
newBlogPage.category('School')
newBlogPage.content('Cowabunga dude!')
newBlogPage.post() // could get this to return a ViewPostPage

def viewBlogPage = PageFactory.initElements(driver, ViewPostPage)
assert viewBlogPage.mainHeading() ==~ 'Post.*: Bart was here.*'
assert viewBlogPage.categoryHeading() == 'Category: School'
assert viewBlogPage.authorHeading() == 'Author: Bart'
assert viewBlogPage.blogText() == 'Cowabunga dude!'

class NewPostPage {
    private WebElement title, category, author, content, btnPost
    def title(title) { this.title.sendKeys(title) }
    def category(category) { this.category.findElements(By.tagName("option")).find{ it.text == category }.setSelected() }
    def author(author) { this.author.findElements(By.tagName("option")).find{ it.text == author }.setSelected() }
    def content(content) { this.content.sendKeys(content) }
    def post() { btnPost.click() }
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