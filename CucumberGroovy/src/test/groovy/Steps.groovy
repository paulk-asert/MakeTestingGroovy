import cucumber.api.groovy.EN
import cucumber.api.groovy.Hooks
import groovy.transform.Field

this.metaClass.mixin(Hooks)
this.metaClass.mixin(EN)
@Field tester
@Field formFields = [:]

Given(~/^we are on the create blog entry page$/) { ->
    tester = new BlogTester('http://localhost:8080/postForm')
    tester.checkTitle 'Welcome to SimpBlog'
}

When(~/^I have entered '([^']*)' as the title$/) { String title ->
    formFields.title = title
}

When(~/^I have entered '([^']*)' into the content$/) { String content ->
    formFields.content = content
}

When(~/^I have selected '([^']*)' as the category$/) { String category ->
    formFields.category = category
}

When(~/^I have selected '([^']*)' as the author$/) { String author ->
    formFields.author = author
}

When(~/^I click the 'Create Post' button$/) { ->
    tester.postBlog(formFields)
}

Then(~/^I expect the entry to be posted$/) { ->
    tester.checkHeadingMatches formFields.title
    tester.checkSubheading 'Category', formFields.category
    tester.checkPostText formFields.content
    tester.checkSubheading 'Author', formFields.author
}

