package de.dhbw2go.backend.payload.response.basic;

import lombok.Data;

@Data
public class MessageResponse implements ResponsePayload {

    private String message;

    @Override
    public String getData() {
        return "Message: " + message;
    }
}
