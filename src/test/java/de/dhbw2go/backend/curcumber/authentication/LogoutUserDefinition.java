package de.dhbw2go.backend.curcumber.authentication;

import de.dhbw2go.backend.curcumber.ApplicationTest;
import io.cucumber.java.en.And;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;

public class LogoutUserDefinition {

    @And("User with email {string} logout.")
    public void userLogout(final String email) {
        final RequestSpecification requestSpecification = ApplicationTest.createRequestSpecification(email);
        final Response response = requestSpecification.post("/authentication/logout");
        ApplicationTest.getJwtCookies().remove(email);
        response.then().assertThat().statusCode(Matchers.equalTo(200));
    }
}
