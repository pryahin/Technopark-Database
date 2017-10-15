package Database.Models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by vova on 13.10.17.
 */
public class PostFullModel {

    private UserModel author;
    private ForumModel forum;
    private PostModel post;
    private ThreadModel thread;

    @JsonCreator
    PostFullModel(
            @JsonProperty("author") UserModel author,
            @JsonProperty("forum") ForumModel forum,
            @JsonProperty("post") PostModel post,
            @JsonProperty("thread") ThreadModel thread
    ) {
        this.author = author;
        this.forum = forum;
        this.post = post;
        this.thread = thread;
    }

    public PostFullModel() {
    }

    public UserModel getAuthor() {
        return author;
    }

    public void setAuthor(UserModel author) {
        this.author = author;
    }

    public ForumModel getForum() {
        return forum;
    }

    public void setForum(ForumModel forum) {
        this.forum = forum;
    }

    public PostModel getPost() {
        return post;
    }

    public void setPost(PostModel post) {
        this.post = post;
    }

    public ThreadModel getThread() {
        return thread;
    }

    public void setThread(ThreadModel thread) {
        this.thread = thread;
    }
}
