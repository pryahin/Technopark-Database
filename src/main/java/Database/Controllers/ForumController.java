package Database.Controllers;

import Database.DAO.ForumDAO;
import Database.DAO.ThreadDAO;
import Database.DAO.UserDAO;
import Database.Models.ErrorModel;
import Database.Models.ForumModel;
import Database.Models.ThreadModel;
import Database.Models.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/forum")
@RestController
public class ForumController {

    private ForumDAO forumDAO;
    private UserDAO userDAO;
    private ThreadDAO threadDAO;

    @Autowired
    public ForumController(ForumDAO forumDAO, UserDAO userDAO, ThreadDAO threadDAO) {
        this.forumDAO = forumDAO;
        this.userDAO = userDAO;
        this.threadDAO = threadDAO;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity createForum(@RequestBody ForumModel forum) {
        forum.setPosts(0);
        forum.setThreads(0);
        try {
            UserModel user = userDAO.getUser(forum.getUser());
            if (user == null) {
                ErrorModel error = new ErrorModel();
                error.setMessage("Can't find user with nickname" + forum.getUser());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
            forum.setUser(user.getNickname());
            forumDAO.createForum(forum);
            return ResponseEntity.status(HttpStatus.CREATED).body(forumDAO.getForum(forum.getSlug()));
        } catch (DuplicateKeyException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(forumDAO.getForum(forum.getSlug()));
        }
    }

    @RequestMapping(value = "/{slug}/create", method = RequestMethod.POST)
    public ResponseEntity createThread(@PathVariable(name = "slug") String slug, @RequestBody ThreadModel thread) {
        ForumModel forum = forumDAO.getForum(slug);
        if (forum == null) {
            ErrorModel error = new ErrorModel();
            error.setMessage("Can't find thread forum by slug: " + slug);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
        thread.setForum(forum.getSlug());
        try {
            UserModel user = userDAO.getUser(thread.getAuthor());
            if (user == null) {
                ErrorModel error = new ErrorModel();
                error.setMessage("Can't find user with nickname" + thread.getAuthor());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
            thread.setAuthor(user.getNickname());
            threadDAO.createThread(thread);
            return ResponseEntity.status(HttpStatus.CREATED).body(thread);
        } catch (DuplicateKeyException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(threadDAO.getThreadBySlug(thread.getSlug()));
        }
    }

    @RequestMapping(value = "{slug}/details", method = RequestMethod.GET)
    public ResponseEntity getDetails(@PathVariable(name = "slug") String slug) {
        ForumModel forum = forumDAO.getForum(slug);
        if (forum == null) {
            ErrorModel error = new ErrorModel();
            error.setMessage("Can't find forum " + slug);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
        return ResponseEntity.status(HttpStatus.OK).body(forum);
    }

    @RequestMapping(value = "{slug}/threads", method = RequestMethod.GET)
    public ResponseEntity getThreads(@PathVariable(name = "slug") String slug,
                                     @RequestParam(value = "limit", defaultValue = "0") int limit,
                                     @RequestParam(value = "since", defaultValue = "") String since,
                                     @RequestParam(value = "desc", defaultValue = "false") boolean desc) {
        ForumModel forum = forumDAO.getForum(slug);
        if (forum == null) {
            ErrorModel error = new ErrorModel();
            error.setMessage("Can't find forum " + slug);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
        return ResponseEntity.status(HttpStatus.OK).body(threadDAO.getThreads(slug, limit, since, desc));
    }

    @RequestMapping(value = "{slug}/users", method = RequestMethod.GET)
    public ResponseEntity getUsers(@PathVariable(name = "slug") String slug) {
        return ResponseEntity.status(HttpStatus.OK).body(slug + " - users: ...");
    }
}
