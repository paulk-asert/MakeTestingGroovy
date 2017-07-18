//@Grab("org.gebish:geb-core:1.1.1")
//@Grab("org.seleniumhq.selenium:selenium-firefox-driver:2.52.0")
//@Grab("org.seleniumhq.selenium:selenium-chrome-driver:2.52.0")
//@Grab("org.seleniumhq.selenium:selenium-support:2.52.0")
//@GrabExclude("org.codehaus.groovy:groovy-all")
// if chromedriver not in path:
// on windows run with -Dwebdriver.chrome.driver=c:\path\to\your\chromedriver.exe
// on linux run with -Dwebdriver.chrome.driver=c/path/to/chromedriver
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
    blogTitle.value 'Bart was here (Geb pages)'
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
