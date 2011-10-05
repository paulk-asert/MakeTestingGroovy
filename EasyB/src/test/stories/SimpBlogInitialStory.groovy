package test.stories
// org.easyb:easyb-core:1.2
scenario "Bart posts a new blog entry", {
    given "we are on the create blog entry page"
    when "I have entered 'Bart was here' as the title"
    and "I have entered 'Cowabunga Dude!' into the content"
    and "I have selected 'Home' as the category"
    and "I have selected 'Bart' as the author"
    and "I click the 'Create Post' button"
    then "I expect the entry to be posted"
}
