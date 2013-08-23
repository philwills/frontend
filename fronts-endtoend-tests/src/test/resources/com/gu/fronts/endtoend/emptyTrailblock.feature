@ignore
Feature:
  As an editor
  I want the top-stories block to collapse And be hidden when the live section is empty
  So that it does not disturb the display of other blocks on the front

  Scenario: 4.1 - top-stories block should not display when it is unpopulated
    Given Jane is a viewer looking at the front
    And the top-stories block for that front has no stories
    Then the top-stories block will not be displayed to Jane

  Scenario: 4.2 - Populating draft of top-stories trailblock does not cause it to display
    Given Jane is viewing the front containing the prominent story block
    And the top-stories trailblock has 0 stories in the live section
    And the top-stories block has 1 or more stories in the draft section
    When Jane refreshes her browser
    Then   Jane should not see the top-stories trailblock
