package simpblog

import com.gargoylesoftware.htmlunit.WebClient

class NewBlogPost {
    String author, title, content, category
    private result
    def execute() {
        def client = new WebClient()
        def page = client.getPage('http://localhost:8080/postForm')
        assert 'Welcome to SimpBlog' == page.titleText
        def form = page.getFormByName('post')
        form.getInputByName('title').
            setValueAttribute("$title (Slim)")
        form.getSelectByName('author').getOptions().find{
            it.text == author }.setSelected(true)
        form.getSelectByName('category').getOptions().find{
            it.text == category }.setSelected(true)
        form.getTextAreaByName('content').setText(content)
        result = form.getInputByName('btnPost').click()
    }

    def mainHeading() {
        def m = result.getElementsByTagName('h1').item(0).textContent =~ /Post .*: (.*) \([^)]*\)/
        m[0][1]
    }
}