//@groovy.lang.GrabResolver('https://nexus.codehaus.org/content/repositories/snapshots/')
//@Grab("org.codehaus.geb:geb-core:0.7.0-SNAPSHOT")
//@Grab("org.seleniumhq.selenium:selenium-firefox-driver:2.7.0")
import geb.Browser
import geb.Page

class NewPostPage extends Page {
    static url = "postForm"
}

Browser.drive {
    setBaseUrl "http://localhost:8080/"
    go()
    assert title == 'Welcome to SimpBlog'
    to NewPostPage

    $("form").with {
        title = "Bart was here (Geb)"
        find('option', text: 'Homer').click()
        find('option', text: 'Travel').click()
        content = "Cowabunga Dude!"
        btnPost().click()
    }
    assert $("h1").text().matches("Post \\d+: Bart was here.*")
}
