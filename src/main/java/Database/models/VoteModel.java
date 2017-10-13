package Database.Models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by vova on 13.10.17.
 */
public class VoteModel {

    private String nickname;
    private int voice;

    @JsonCreator
    VoteModel(
            @JsonProperty("nickname") String nickname,
            @JsonProperty("voice") int voice
    ) {
        this.nickname = nickname;
        this.voice = voice;
    }

    public VoteModel() {
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getVoice() {
        return voice;
    }

    public void setVoice(int voice) {
        this.voice = voice;
    }
}
