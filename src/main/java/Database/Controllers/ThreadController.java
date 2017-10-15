package Database.Controllers;

import Database.DAO.PostDAO;
import Database.DAO.ThreadDAO;
import Database.DAO.UserDAO;
import Database.DAO.VoteDAO;
import Database.Models.*;
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
    private VoteDAO voteDAO;

    @Autowired
    public ThreadController(ThreadDAO threadDAO, PostDAO postDAO, UserDAO userDAO, VoteDAO voteDAO) {
        this.threadDAO = threadDAO;
        this.postDAO = postDAO;
        this.userDAO = userDAO;
        this.voteDAO = voteDAO;
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
        ThreadModel thread = threadDAO.getThreadBySlugOrId(slug);
        if (thread == null) {
            ErrorModel error = new ErrorModel();
            error.setMessage("Can't find thread " + slug);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
        return ResponseEntity.status(HttpStatus.OK).body(thread);
    }

    @RequestMapping(value = "/{slug_or_id}/details", method = RequestMethod.POST)
    public ResponseEntity changeThread(@PathVariable(name="slug_or_id") String slug) {
        return ResponseEntity.status(HttpStatus.OK).body("changed "+slug);
    }

    @RequestMapping(value = "/{slug_or_id}/posts", method = RequestMethod.GET)
    public ResponseEntity getPosts(@PathVariable(name="slug_or_id") String slug,
                                   @RequestParam(value = "limit", defaultValue = "0") int limit,
                                   @RequestParam(value = "since", defaultValue = "") String since,
                                   @RequestParam(value = "sort", defaultValue = "flat") String sort,
                                   @RequestParam(value = "desc", defaultValue = "false") boolean desc) {

        ThreadModel thread = threadDAO.getThreadBySlugOrId(slug);
        if (thread == null) {
            ErrorModel error = new ErrorModel();
            error.setMessage("Can't find thread " + slug);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
        return ResponseEntity.status(HttpStatus.OK).body(postDAO.getPosts(thread.getId(), limit, since, sort, desc));
    }

    @RequestMapping(value = "/{slug_or_id}/vote", method = RequestMethod.POST)
    public ResponseEntity vote(@PathVariable(name="slug_or_id") String slug, @RequestBody VoteModel vote) {
        ThreadModel thread = threadDAO.getThreadBySlugOrId(slug);
        if (thread == null) {
            ErrorModel error = new ErrorModel();
            error.setMessage("Can't find thread " + slug);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        vote.setThread(thread.getId());
        thread.setVotes(voteDAO.vote(vote));
        return ResponseEntity.status(HttpStatus.OK).body(thread);
    }
}
