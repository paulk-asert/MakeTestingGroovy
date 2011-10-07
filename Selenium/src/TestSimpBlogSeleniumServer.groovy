//@Grab('org.seleniumhq.selenium:selenium-firefox-driver:2.8.0')
@Grab('org.seleniumhq.selenium:selenium-chrome-driver:2.8.0')
@Grab('org.seleniumhq.selenium:selenium-server:2.8.0')
@GrabExclude('xml-apis:xml-apis')
import com.thoughtworks.selenium.DefaultSelenium
import org.openqa.selenium.server.SeleniumServer

// start auxiliary server
def server = new SeleniumServer()
server.start()

// uncomment one of below
def browser = "*googlechrome C:/Program Files (x86)/Google/Chrome/Application/chrome.exe"
//def browser = "*firefox3 C:/Program Files (x86)/Mozilla Firefox/firefox.exe"

def selenium = new DefaultSelenium("localhost", 4444, browser, "http://localhost:8080")
selenium.start()

// post blog
selenium.open "/postForm"
selenium.type "title", "Bart was here (Selenium)"
selenium.select "category", "Home"
selenium.type "content", "Cowabunga Dude!"
selenium.click "btnPost"
selenium.waitForPageToLoad "5000"

// checks
assert selenium.isTextPresent('regex:Post.*: Bart was here')
assert selenium.isElementPresent('//h3[text()="Author: Bart"]')
assert selenium.isElementPresent('//h3[text()="Category: Home"]')
assert selenium.isElementPresent('//table//tr/td/p[text()="Cowabunga Dude!"]')

selenium.stop()
server.stop()
