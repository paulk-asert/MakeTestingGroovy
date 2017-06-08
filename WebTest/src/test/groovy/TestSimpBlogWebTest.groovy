def webtest_home = '../webtest20160719'
def ant = new AntBuilder()

ant.taskdef(resource: 'webtest.taskdef') {
    classpath {
        pathelement(location: "$webtest_home/lib")
        fileset(dir: "$webtest_home/lib", includes: "**/*.jar",
                excludes: "**/groovy*.jar,**/ant*.jar")
    }
}

ant.webtest(name: 'Test SimpBlog') {
    invoke url: "http://localhost:8080/", description: "Home Page"
    verifyTitle text: "Welcome to SimpBlog"

    group description: "Post New Blog Entry", {
        clickLink label: "New Blog Entry"
        setInputField name: "title",
                value: "Bart was here (WebTest Groovy)"
        setSelectField name: "category", text: "School"
        setInputField name: "content", value: "Cowabunga Dude!"
        clickButton name: "btnPost"
    }

    group description: "Check Blog Post", {
        verifyElementText type: "h1", regex: "true",
                text: "Post.*: Bart was here.*"
        verifyXPath xpath: "//h3[2]/text()", text: "Category: School"
        verifyXPath xpath: "//h3[3]/text()", text: "Author: Bart"
        verifyElementText type: "p", text: "Cowabunga Dude!"
    }
}
