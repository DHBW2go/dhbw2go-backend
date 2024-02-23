Feature: Register User

  Scenario: Registration
    Then User register with username "register", email "register@test.youandmeme.de" and password "registerTesting".
    And User exists with email "register@test.youandmeme.de"!