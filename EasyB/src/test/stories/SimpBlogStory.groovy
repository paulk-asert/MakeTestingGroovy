description "Post Blog Entry Feature"

narrative "for feature", {
    as_a "Blogger"
    i_want "to be able to post a blog"
    so_that "I can keep others informed"
}

before "posting blog", {
    given "we are on the create blog entry page", {
        tester = new BlogTester('http://localhost:8080/postForm')
        tester.checkTitle 'Welcome to SimpBlog'
    }
}

scenario "Bart was here blog", {

    when "I have entered 'Bart was here' as the title", {
        title = 'Bart was here (EasyB)'
    }

    and "I have entered 'Cowabunga Dude!' into the content", {
        content = 'Cowabunga Dude!'
    }

    and "I have selected 'School' as the category", {
        category = 'School'
    }

    and "I click the 'Create Post' button", {
        tester.postBlog(title:title, author:'Bart', category:category, content:content)
    }

    then "I expect the entry to be posted", {
        tester.checkHeadingMatches 'Bart was here (EasyB)'
        tester.checkSubheading 'Category', 'School'
        tester.checkSubheading 'Author', 'Bart'
        tester.checkPostText 'Cowabunga Dude!'
    }
}
