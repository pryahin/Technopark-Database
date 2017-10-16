package Database.Controllers;

import Database.DAO.*;
import Database.Models.ErrorModel;
import Database.Models.PostFullModel;
import Database.Models.PostModel;
import Database.Models.PostUpdateModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RequestMapping("/api/post")
@RestController
public class PostController {

    private PostDAO postDAO;
    private UserDAO userDAO;
    private ForumDAO forumDAO;
    private ThreadDAO threadDAO;

    @Autowired
    public PostController(PostDAO postDAO, UserDAO userDAO, ForumDAO forumDAO, ThreadDAO threadDAO) {
        this.postDAO = postDAO;
        this.userDAO = userDAO;
        this.forumDAO = forumDAO;
        this.threadDAO = threadDAO;
    }

    @RequestMapping(value = "/{id}/details", method = RequestMethod.POST)
    public ResponseEntity changePost(@PathVariable(name = "id") int id, @RequestBody PostUpdateModel postUpdate) {
        PostModel post = postDAO.getPost(id);
        if (post == null) {
            ErrorModel error = new ErrorModel("Can't find post with id " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        if (postUpdate.getMessage() != null) {
            if (post.getMessage().compareTo(postUpdate.getMessage()) != 0) {
                post.setMessage(postUpdate.getMessage());
                postDAO.updatePost(post);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(post);
    }

    @RequestMapping(value = "/{id}/details", method = RequestMethod.GET)
    public ResponseEntity getPost(@PathVariable(name = "id") int id, @RequestParam(name = "related", required = false) Set<String> related) {
        PostFullModel postFullModel = new PostFullModel();
        postFullModel.setPost(postDAO.getPost(id));

        if (postFullModel.getPost() == null) {
            ErrorModel error = new ErrorModel("Can't find post with id: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        if (related != null) {
            if (related.contains("user")) {
                postFullModel.setAuthor(userDAO.getUser(postFullModel.getPost().getAuthor()));
            }
            if (related.contains("forum")) {
                postFullModel.setForum(forumDAO.getForum(postFullModel.getPost().getForum()));
            }
            if (related.contains("thread")) {
                postFullModel.setThread(threadDAO.getThreadById(postFullModel.getPost().getThread()));
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body(postFullModel);
    }
}
