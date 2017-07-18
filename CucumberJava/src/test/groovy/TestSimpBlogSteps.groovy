import cucumber.api.java.en.Given
import cucumber.api.java.en.Then
import cucumber.api.java.en.When

class TestSimpBlogSteps {
    BlogTester tester
    Map formFields = [:]

    @Given('^we are on the create blog entry page$')
    void initTester() {
        tester = new BlogTester('http://localhost:8080/postForm')
    }

    @When(/^I have entered '([^']*)' as the title$/)
    void enterTitle(String title) {
        formFields.title = title
    }

    @When(/^I have entered '([^']*)' into the content$/)
    void enterContent(String content) {
        formFields.content = content
    }

    @When(/^I have selected '([^']*)' as the category$/)
    void enterCategory(String category) {
        formFields.category = category
    }

    @When(/^I have selected '([^']*)' as the author$/)
    void enterAuthor(String author) {
        formFields.author = author
    }

    @When(/^I click the 'Create Post' button$/)
    void createPost() {
        tester.postBlog(formFields)
    }

    @Then(/^I expect the entry to fail$/)
    void confirmFail() {
        assert tester.lastResult.body.textContent.contains('Creation failed! Validation Error?')
    }

    @Then(/^I expect the entry to be posted$/)
    void confirmPost() {
        tester.checkHeadingMatches formFields.title
        tester.checkSubheading 'Category', formFields.category
        tester.checkPostText formFields.content
        tester.checkSubheading 'Author', formFields.author
    }
}
