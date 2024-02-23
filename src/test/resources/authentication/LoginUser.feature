Feature: Login User

  Background: Registration
    Then User register with username "login", email "login@test.youandmeme.de" and password "loginTesting".
    And User exists with email "login@test.youandmeme.de"!

  Scenario: Check and login
    Then User login with email "login@test.youandmeme.de" and password "loginTesting".