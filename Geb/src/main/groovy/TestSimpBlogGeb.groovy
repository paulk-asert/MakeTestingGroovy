//@Grab("org.gebish:geb-core:1.1.1")
//@Grab("org.seleniumhq.selenium:selenium-firefox-driver:2.52.0")
//@Grab("org.seleniumhq.selenium:selenium-chrome-driver:2.52.0")
//@Grab("org.seleniumhq.selenium:selenium-htmlunit-driver:2.52.0")
//@Grab("org.seleniumhq.selenium:selenium-support:2.52.0")
//@GrabExclude("org.codehaus.groovy:groovy-all")
// see build.gradle for system properties that you might need to set if using a real browser
import geb.Browser

Browser.drive {
    go 'http://localhost:8080/postForm'
    assert title == 'Welcome to SimpBlog'

    $("form").with {
        title = "Bart was here (Geb)"
        author = 'Bart'
        category = 'School'
        content = "Cowabunga Dude!"
        btnPost().click()
    }
    assert $("h1").text().matches("Post \\d+: Bart was here.*")
    assert $("h3")[1].text() == 'Category: School'
    assert $("h3")[2].text() == 'Author: Bart'
    assert $("p").text() == "Cowabunga Dude!"
}
