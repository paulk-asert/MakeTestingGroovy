@Grab("org.codehaus.geb:geb-core:0.6.0")
@Grab("org.seleniumhq.selenium:selenium-firefox-driver:2.0rc3")
import geb.Browser
 
Browser.drive {
    go "http://localhost:8080/"
    assert title() == 'Welcome to SimpBlog'

    $("form.post").with {
        title = "Bart was here (Geb)"
        author = "Bart"
        category = "Home"
        content = "Cowabunga Dude!"
        btnPost().click()
    }
    assert $("h1").text().matches("Post \\d+: Bart was here.*")
}
