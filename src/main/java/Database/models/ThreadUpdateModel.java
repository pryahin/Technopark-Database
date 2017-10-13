package Database.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by vova on 13.10.17.
 */
public class ThreadUpdateModel {

    private String message;
    private String title;

    @JsonCreator
    ThreadUpdateModel(
            @JsonProperty("message") String message,
            @JsonProperty("title") String title
    ) {
        this.message = message;
        this.title = title;
    }

    public ThreadUpdateModel() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
