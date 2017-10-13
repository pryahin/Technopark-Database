package Database.Models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by vova on 13.10.17.
 */
public class UserUpdateModel {

    private String about;
    private String email;
    private String fullname;

    @JsonCreator
    UserUpdateModel(
            @JsonProperty("about") String about,
            @JsonProperty("email") String email,
            @JsonProperty("fullname") String fullname
    ) {
        this.about = about;
        this.email = email;
        this.fullname = fullname;
    }

    public UserUpdateModel() {
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
}
