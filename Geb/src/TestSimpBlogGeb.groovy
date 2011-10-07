@GrabResolver('https://nexus.codehaus.org/content/repositories/snapshots/')
@Grab("org.codehaus.geb:geb-core:0.7.0-SNAPSHOT")
@Grab("org.seleniumhq.selenium:selenium-firefox-driver:2.8.0")
import geb.Browser

Browser.drive {
    go 'http://localhost:8080/postForm'
    assert title == 'Welcome to SimpBlog'

    $("form").with {
        title = "Bart was here (Geb)"
        find('option', text: 'Bart').click()
        find('option', text: 'School').click()
        content = "Cowabunga Dude!"
        btnPost().click()
    }
    assert $("h1").text().matches("Post \\d+: Bart was here.*")
    assert $("h3")[1].text() == 'Category: School'
    assert $("h3")[2].text() == 'Author: Bart'
    assert $("p").text() == "Cowabunga Dude!"
}
