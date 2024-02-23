package de.dhbw2go.backend.curcumber.authentication;

import de.dhbw2go.backend.curcumber.ApplicationTest;
import io.cucumber.java.en.And;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;
import org.json.JSONObject;

public class ChangePasswordDefinition {

    @And("User with email {string} change password from old password {string} to new password {string}.")
    public void userChangePasswordFromOldPasswordTo(final String email, final String oldPassword, final String newPassword) {
        final RequestSpecification requestSpecification = ApplicationTest.createRequestSpecification(email)
                .header("Content-Type", "application/json")
                .body(new JSONObject().put("oldPassword", oldPassword).put("newPassword", newPassword).toString());
        final Response response = requestSpecification.post("/authentication/change-password");
        response.then().assertThat().statusCode(Matchers.equalTo(200));
    }
}
