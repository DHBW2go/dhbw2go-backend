Feature: Logout User

  Background: Registration
    Then User register with username "logout", email "logout@test.youandmeme.de" and password "logoutTesting".
    And User exists with email "logout@test.youandmeme.de"!

  Scenario: Check, login and logout
    Then User login with email "logout@test.youandmeme.de" and password "logoutTesting".
    And User with email "logout@test.youandmeme.de" logout.