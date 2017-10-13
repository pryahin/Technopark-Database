package Database.Models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorModel {
    private String message;

    @JsonCreator
    public ErrorModel(
        @JsonProperty("message") String message
    ) {
        this.message = message;
    }

    public ErrorModel() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
