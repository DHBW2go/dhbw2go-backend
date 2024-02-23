package de.dhbw2go.backend.curcumber.authentication;

import de.dhbw2go.backend.curcumber.ApplicationTest;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;
import org.json.JSONObject;

public class RegisterUserDefinition {

    @Then("User register with username {string}, email {string} and password {string}.")
    public void userSignUpWithUsernameEmailAddressAndPassword(final String username, final String email, final String password) {
        final RequestSpecification requestSpecification = ApplicationTest.createRequestSpecification(email)
                .header("Content-Type", "application/json")
                .body(new JSONObject().put("username", username).put("email", email).put("password", password).toString());
        final Response response = requestSpecification.put("/authentication/register");
        System.out.println(new JSONObject().put("username", username).put("email", email).put("password", password).toString());
        response.then().assertThat().statusCode(Matchers.equalTo(200));
    }

    @And("User exists with email {string}!")
    public void userExistsWithEmailAddress(final String email) {
        final RequestSpecification requestSpecification = ApplicationTest.createRequestSpecification(email);
        final Response response = requestSpecification.get("/authentication/check/" + email);
        response.then().assertThat().statusCode(Matchers.equalTo(400));
    }
}
