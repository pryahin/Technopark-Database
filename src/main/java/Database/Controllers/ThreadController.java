package Database.Controllers;

import Database.DAO.PostDAO;
import Database.DAO.ThreadDAO;
import Database.DAO.UserDAO;
import Database.Models.ErrorModel;
import Database.Models.PostModel;
import Database.Models.ThreadModel;
import Database.Models.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/thread/")
@RestController
public class ThreadController {

    private ThreadDAO threadDAO;
    private PostDAO postDAO;
    private UserDAO userDAO;

    @Autowired
    public ThreadController(ThreadDAO threadDAO, PostDAO postDAO, UserDAO userDAO) {
        this.threadDAO = threadDAO;
        this.postDAO = postDAO;
        this.userDAO = userDAO;
    }

    @RequestMapping(value = "/{slug_or_id}/create", method = RequestMethod.POST)
    public ResponseEntity createPost(@PathVariable(name="slug_or_id") String slug, @RequestBody List<PostModel> posts) {
        try {
            ThreadModel thread = threadDAO.getThreadBySlugOrId(slug);
            if (thread == null) {
                ErrorModel error = new ErrorModel();
                error.setMessage("Can't find thread " + slug);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }

            for (PostModel post : posts ) {
                UserModel user = userDAO.getUser(thread.getAuthor());
                if (user == null) {
                    ErrorModel error = new ErrorModel();
                    error.setMessage("Can't find user with nickname" + thread.getAuthor());
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
                }

                if (post.getParent() != 0) {
                    PostModel parent = postDAO.getPost(post.getParent());
                    if (parent == null) {
                        ErrorModel error = new ErrorModel();
                        error.setMessage("Can't find parent with if " + post.getParent());
                        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
                    }
                }

                post.setForum(thread.getForum());
                post.setThread(thread.getId());
            }

            postDAO.createPost(posts);
            return ResponseEntity.status(HttpStatus.CREATED).body(posts);
        } catch (DuplicateKeyException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("conflict ...");
        }
    }

    @RequestMapping(value = "/{slug_or_id}/details", method = RequestMethod.GET)
    public ResponseEntity getThread(@PathVariable(name="slug_or_id") String slug) {
        return ResponseEntity.status(HttpStatus.OK).body("get "+slug+" thread");
    }

    @RequestMapping(value = "/{slug_or_id}/details", method = RequestMethod.POST)
    public ResponseEntity changeThread(@PathVariable(name="slug_or_id") String slug) {
        return ResponseEntity.status(HttpStatus.OK).body("changed "+slug);
    }

    @RequestMapping(value = "/{slug_or_id}/posts", method = RequestMethod.GET)
    public ResponseEntity getPosts(@PathVariable(name="slug_or_id") String slug) {
        return ResponseEntity.status(HttpStatus.OK).body("[Posts] from "+slug);
    }

    @RequestMapping(value = "/{slug_or_id}/vote", method = RequestMethod.POST)
    public ResponseEntity vote(@PathVariable(name="slug_or_id") String slug) {
        return ResponseEntity.status(HttpStatus.OK).body("vote "+slug);
    }
}
