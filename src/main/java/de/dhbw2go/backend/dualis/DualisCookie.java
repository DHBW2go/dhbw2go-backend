package de.dhbw2go.backend.dualis;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.net.HttpCookie;

@Data
@AllArgsConstructor
public class DualisCookie {

    @NotEmpty
    private String userArguments;

    @NotEmpty
    private String name;

    @NotEmpty
    private String value;

    @JsonIgnore
    public HttpCookie buildHttpCookie() {
        return new HttpCookie(name, value);
    }
}
