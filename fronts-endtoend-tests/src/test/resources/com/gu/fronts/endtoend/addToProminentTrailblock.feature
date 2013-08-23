@ignore
Feature: adding stories to prominent block
  As an editor
  I want to be able to add up to 5 stories to the prominent story block
  So that I can highlight news around an important subject where it will be most visible

  Scenario: 2.1 - Add X stories to the prominent-story block
    Given Bob is editing the prominent story block
    And Jane is Viewing the related front
    When Bob sets up the block to have X stories
    And Jane refreshes her browser
    Then Jane will see X stories in the prominent block
    And the stories are displayed in the correct format for that number of stories
#(where X = 1, 2, 3, 4, 5 )
