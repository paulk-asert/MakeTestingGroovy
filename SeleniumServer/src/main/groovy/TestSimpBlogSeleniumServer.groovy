//@Grab('org.seleniumhq.selenium:selenium-support:3.4.0')
//@Grab('org.seleniumhq.selenium:selenium-server:3.4.0')
import org.openqa.grid.internal.utils.configuration.StandaloneConfiguration
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.remote.DesiredCapabilities
import org.openqa.selenium.remote.RemoteWebDriver
import org.openqa.selenium.remote.server.SeleniumServer

// start auxiliary server
def config = new StandaloneConfiguration()
def server = new SeleniumServer(config)
server.boot()

// below uses Firefox but could be switched to another browser
//System.setProperty('webdriver.gecko.driver', 'C:\\path\\to\\geckodriver.exe')
//System.setProperty('webdriver.gecko.driver', '/path/to/geckodriver')
def browser = DesiredCapabilities.firefox()
WebDriver driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), browser)

driver.get('http://localhost:8080/postForm')

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

driver.quit()

server.stop()
