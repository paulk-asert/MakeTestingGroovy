//@Grab('org.seleniumhq.selenium:htmlunit-driver:2.27')
//@Grab('org.seleniumhq.selenium:selenium-support:3.4.0')
//@GrabExclude('xml-apis:xml-apis')
import org.openqa.selenium.By
//import org.openqa.selenium.chrome.ChromeDriver
//import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.htmlunit.HtmlUnitDriver
//import org.openqa.selenium.ie.InternetExplorerDriver

// uncomment one driver from below
//def driver = new ChromeDriver()
//def driver = new FirefoxDriver()
def driver = new HtmlUnitDriver()
//def driver = new InternetExplorerDriver()

driver.get('http://localhost:8080/postForm')
assert driver.title == 'Welcome to SimpBlog'

// fill in query form and submit it
driver.findElement(By.name('title')).sendKeys('Bart was here (WebDriver)')
def select = driver.findElement(By.name('category'))
select.findElements(By.tagName("option")).find{ it.text == 'School' }.click()
driver.findElement(By.name('content')).sendKeys('Cowabunga dude!')
driver.findElement(By.name('btnPost')).click()

sleep 100

assert driver.findElement(By.tagName("h1")).text.matches('Post.*: Bart was here.*')
def h3headers = driver.findElements(By.tagName("h3"))
assert h3headers[1].text == 'Category: School'
assert h3headers[2].text == 'Author: Bart'

// try a more advanced finder
// content is at: //TABLE/TBODY/TR/TD/P
def row = driver.findElement(By.xpath("//table/tbody/tr"))
def col = row.findElement(By.tagName("td"))
def para = col.findElement(By.tagName("p"))
assert para.text == 'Cowabunga dude!'

driver.close()
