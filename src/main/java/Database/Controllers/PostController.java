package Database.Controllers;

import Database.DAO.PostDAO;
import Database.DAO.ThreadDAO;
import Database.DAO.UserDAO;
import Database.DAO.VoteDAO;
import Database.Models.ErrorModel;
import Database.Models.PostModel;
import Database.Models.PostUpdateModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/post")
@RestController
public class PostController {

    private PostDAO postDAO;

    @Autowired
    public PostController(PostDAO postDAO) {
        this.postDAO = postDAO;
    }

    @RequestMapping(value = "/{id}/details", method = RequestMethod.POST)
    public ResponseEntity changePost(@PathVariable(name = "id") int id, @RequestBody PostUpdateModel postUpdate) {
        PostModel post = postDAO.getPost(id);
        if (post == null) {
            ErrorModel error = new ErrorModel();
            error.setMessage("Can't find post with id " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        if (postUpdate.getMessage() != null)
            post.setMessage(postUpdate.getMessage());

        postDAO.updatePost(post);
        return ResponseEntity.status(HttpStatus.OK).body(post);
    }

    @RequestMapping(value = "/{id}/details", method = RequestMethod.GET)
    public ResponseEntity getPost(@PathVariable(name = "id") int id) {
        return ResponseEntity.status(HttpStatus.OK).body(postDAO.getPostDetails(id));
    }

}
