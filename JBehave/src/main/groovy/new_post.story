Given we are on the create blog entry page
When I have entered "Bart was here" as the title
And I have entered "Cowabunga Dude!" as the content
And I have selected "Home" from the "category" dropdown
And I have selected "Bart" from the "author" dropdown
And I click the 'Create Post' button
Then I should see a heading matching "Bart was here"
