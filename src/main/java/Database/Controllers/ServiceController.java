package Database.Controllers;

import Database.DAO.ForumDAO;
import Database.DAO.PostDAO;
import Database.DAO.ThreadDAO;
import Database.DAO.UserDAO;
import Database.Models.StatusModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/service")
@RestController
public class ServiceController {

    private ForumDAO forumDAO;
    private PostDAO postDAO;
    private ThreadDAO threadDAO;
    private UserDAO userDAO;

    @Autowired
    public ServiceController(ForumDAO forumDAO, UserDAO userDAO, ThreadDAO threadDAO, PostDAO postDAO) {
        this.forumDAO = forumDAO;
        this.userDAO = userDAO;
        this.threadDAO = threadDAO;
        this.postDAO = postDAO;
    }

    @RequestMapping(value = "/clear", method = RequestMethod.POST)
    public ResponseEntity clearDB() {
        return ResponseEntity.status(HttpStatus.OK).body("...clear...");
    }

    @RequestMapping(value = "/status", method = RequestMethod.GET)
    public ResponseEntity statusDB() {
        StatusModel status = new StatusModel();
        status.setForum(forumDAO.getCount());
        status.setPost(postDAO.getCount());
        status.setThread(threadDAO.getCount());
        status.setUser(userDAO.getCount());

        return ResponseEntity.status(HttpStatus.OK).body(status);
    }

}