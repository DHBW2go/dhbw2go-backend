package de.dhbw2go.backend.curcumber.authentication;

import de.dhbw2go.backend.curcumber.ApplicationTest;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;
import org.json.JSONObject;

public class LoginUserDefinition {

    @Then("User login with email {string} and password {string}.")
    public void userSignInWithEmailAddressAndPassword(final String email, final String password) {
        final RequestSpecification requestSpecification = ApplicationTest.createRequestSpecification(email)
                .header("Content-Type", "application/json")
                .body(new JSONObject().put("email", email).put("password", password).toString());
        final Response response = requestSpecification.post("/authentication/login");
        ApplicationTest.getJwtCookies().put(email, response.getCookie("youandmeme"));
        response.then().assertThat().statusCode(Matchers.equalTo(200));
    }
}
