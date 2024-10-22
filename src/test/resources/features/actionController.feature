Feature: List actions

  Scenario: List all actions
    When I request GET "/actions"
    Then the response status should be 200
    And the response body should be a list of actions

  Scenario: List actions by type
    Given there are actions of type "SPECIFIC_TYPE"
    When I request GET "/actions?type=SPECIFIC_TYPE"
    Then the response status should be 200
    And the response body should contain actions of type "SPECIFIC_TYPE"

  Scenario: Invalid action type
    When I request GET "/actions?type=INVALID_TYPE"
    Then the response status should be 400
    And the response body should contain an error message
