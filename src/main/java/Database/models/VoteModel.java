package Database.Models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by vova on 13.10.17.
 */
public class VoteModel {

    private String nickname;
    private int voice;
    private int thread;

    @JsonCreator
    VoteModel(
            @JsonProperty("nickname") String nickname,
            @JsonProperty("voice") int voice,
            @JsonProperty("thread") int thread
    ) {
        this.nickname = nickname;
        this.voice = voice;
        this.thread = thread;

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

    public int getThread() {
        return thread;
    }

    public void setThread(int thread) {
        this.thread = thread;
    }
}
