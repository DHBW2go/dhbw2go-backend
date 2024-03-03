package de.dhbw2go.backend.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EventResponse {

    private Event event;

    public interface Event {

        enum Authentication implements Event {
            EMAIL_AVAILABLE, EMAIL_NOT_AVAILABLE,
            USERNAME_AVAILABLE, USERNAME_NOT_AVAILABLE,
            AUTHENTICATION_FAILED, PASSWORD_UNMATCHED,
            REGISTERED, LOGIN, LOGOUT, CHANGED_PASSWORD
        }

        enum User implements Event {
            CHANGED_PICTURE
        }
    }
}
