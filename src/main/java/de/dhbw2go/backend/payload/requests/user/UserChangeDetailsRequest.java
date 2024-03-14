package de.dhbw2go.backend.payload.requests.user;

import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
public class UserChangeDetailsRequest {

    private String name;
    private String location;
    private String faculty;
    private String program;
    private String course;

    @URL
    private String image;
}
