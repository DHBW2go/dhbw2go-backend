Feature: Change Password

  Background: Registration
    Then User register with username "changePassword", email "changePassword@test.youandmeme.de" and password "changePasswordTesting".
    And User exists with email "changePassword@test.youandmeme.de"!

  Scenario: Check, login and change
    Then User login with email "changePassword@test.youandmeme.de" and password "changePasswordTesting".
    And User with email "changePassword@test.youandmeme.de" change password from old password "changePasswordTesting" to new password "changedPasswordTesting".