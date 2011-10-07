@GrabResolver('https://nexus.codehaus.org/content/repositories/snapshots/')
@Grab("org.codehaus.geb:geb-core:0.7.0-SNAPSHOT")
@Grab("org.seleniumhq.selenium:selenium-firefox-driver:2.8.0")
@Grab("org.seleniumhq.selenium:selenium-support:2.8.0")
import geb.Browser
import geb.Page

class NewPostPage extends Page {
    static url = "http://localhost:8080/postForm"
    static at = { title == 'Welcome to SimpBlog' }
    static content = {
        blogTitle { $("form").title() } // !title
        blogger { $("form").author() }
        label { $("form").category() }
        blogText { $("form").content() } // !content
        post(to: ViewPostPage) { btnPost() }
    }
}

class ViewPostPage extends Page {
    static at = { $("h1").text().contains('Post') }
    static content = {
        mainHeading { $("h1").text() }
        categoryHeading { $("h3")[1].text() }
        authorHeading { $("h3")[2].text() }
        blogText { $("p").text() }
    }
}

Browser.drive {
    to NewPostPage

    assert at(NewPostPage)
    blogTitle.value 'Bart was here (Geb)'
    blogger.value 'Bart'
    label.value 'School'
    blogText.value 'Cowabunga Dude!'
    post.click()

    assert at(ViewPostPage)
    assert mainHeading ==~ "Post \\d+: Bart was here.*"
    assert categoryHeading == 'Category: School'
    assert authorHeading == 'Author: Bart'
    assert blogText == "Cowabunga Dude!"
}
