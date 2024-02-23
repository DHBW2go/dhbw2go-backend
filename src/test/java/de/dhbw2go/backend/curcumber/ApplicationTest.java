package de.dhbw2go.backend.curcumber;

import de.dhbw2go.backend.Application;
import io.cucumber.spring.CucumberContextConfiguration;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.Map;

@ActiveProfiles("test")
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@CucumberContextConfiguration
public class ApplicationTest {

    private static final Map<String, String> jwtCookies = new HashMap<>();

    public static RequestSpecification createRequestSpecification(final String email) {
        final RequestSpecification requestSpecification = RestAssured.given();
        if (ApplicationTest.getJwtCookies().containsKey(email)) {
            requestSpecification.cookie("youandmeme", ApplicationTest.getJwtCookies().get(email));
        }
        return requestSpecification;
    }

    public static Map<String, String> getJwtCookies() {
        return ApplicationTest.jwtCookies;
    }
}
