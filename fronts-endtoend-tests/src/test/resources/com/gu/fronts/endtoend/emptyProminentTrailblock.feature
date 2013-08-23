@ignore
Feature: empty prominent story block
  As an editor
  I want the prominent story block to collapse and be hidden when the live section is empty
  So that it does not disturb the display of other blocks on the front

  Scenario: Prominent-story block should not display when it is unpopulated
    Given Jane is a viewer looking at the front
    And the prominent-story block for that front has no stories
    Then the it will not be visible to Jane
    And the top-stories trailblock will display at the top of the front

  Scenario: Populating draft of prominent-story trailblock does not cause it to display
    Given Jane is viewing the front  containing the prominent story block
    And the prominent-story trailblock has 0 stories in the live section
    And the prominent-story block has 1 or more stories in the draft section
    When Jane refreshes her browser
    Then   Jane should not see the prominent-story trailblock
