import net.thucydides.core.annotations.Step

class SimpBlogSteps {
    def tester, title, content, options = [:]

    @Step("Given we are on the create blog entry page")
    void gotoEntryPage() {
        tester = new BlogTester('http://localhost:8080/postForm')
    }

    @Step('When I have entered "{0}" as the title')
    void enterTitle(String title) {
        this.title = title + ' (Serenity)'
    }

    @Step('And I have entered "{0}" as the content')
    void enterContent(String content) {
        this.content = content
    }

    @Step('And I have selected "{0}" from the "{1}" dropdown')
    void selectOption(String option, String name) {
        options[name] = option
    }

    @Step("And I click the 'Create Post' button")
    void clickPostButton() {
        tester.postBlog(title: title, content: content,
                author: options.author, category: options.category)
    }

    @Step('Then I should see a heading matching "{0}"')
    void checkPost(String suffix) {
        tester.checkHeadingMatches suffix + ' (Serenity)'
    }
}