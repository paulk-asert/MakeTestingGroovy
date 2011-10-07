import org.jbehave.core.steps.Steps
import org.jbehave.core.annotations.*

class NewPostSteps extends Steps {

    def tester, title, content, options = [:]

    @Given("we are on the create blog entry page")
    void gotoEntryPage() {
        tester = new BlogTester('http://localhost:8080/postForm')
    }

    @When('I have entered "$title" as the title')
    void enterTitle(String title) {
        this.title = title + ' (JBehave)'
    }

    @When('I have entered "$content" as the content')
    void enterContent(String content) {
        this.content = content
    }

    @When('I have selected "$option" from the "$name" dropdown')
    void selectOption(String option, String name) {
        options[name] = option
    }

    @When("I click the 'Create Post' button")
    void clickPostButton() {
        tester.postBlog(title: title, content: content,
                author: options.author, category: options.category)
    }

    @Then('I should see a heading matching "$suffix"')
    void checkPost(String suffix) {
        tester.checkHeadingMatches suffix + ' (JBehave)'
    }
}