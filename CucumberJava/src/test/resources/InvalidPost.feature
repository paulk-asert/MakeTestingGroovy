Feature: Use the blogging system

  Scenario: Lisa posts a too long title
    Given we are on the create blog entry page
    When I have entered 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz' as the title
      And I have entered 'Some Content' into the content
      And I have selected 'School' as the category
      And I have selected 'Lisa' as the author
      And I click the 'Create Post' button
    Then I expect the entry to fail
