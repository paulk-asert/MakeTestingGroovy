Feature: Use the blogging system

  Scenario: Bart posts a new blog entry
    Given we are on the create blog entry page
    When I have entered 'Bart was here (Cuke Java)' as the title
      And I have entered 'Cowabunga Dude!' into the content
      And I have selected 'Home' as the category
      And I have selected 'Bart' as the author
      And I click the 'Create Post' button
    Then I expect the entry to be posted
