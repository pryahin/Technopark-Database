package Database.DAO;

import Database.Models.StatusModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceDAO {

    private ForumDAO forumDAO;
    private PostDAO postDAO;
    private ThreadDAO threadDAO;
    private UserDAO userDAO;
    private VoteDAO voteDAO;

    @Autowired
    public ServiceDAO(ForumDAO forumDAO, UserDAO userDAO, ThreadDAO threadDAO, PostDAO postDAO, VoteDAO voteDAO) {
        this.forumDAO = forumDAO;
        this.userDAO = userDAO;
        this.threadDAO = threadDAO;
        this.postDAO = postDAO;
        this.voteDAO = voteDAO;
    }

    public StatusModel getStatus() {
        StatusModel status = new StatusModel();
        status.setForum(forumDAO.getCount());
        status.setPost(postDAO.getCount());
        status.setThread(threadDAO.getCount());
        status.setUser(userDAO.getCount());

        return status;
    }

    public void clearDB() {
        forumDAO.clearTable();
        postDAO.clearTable();
        threadDAO.clearTable();
        userDAO.clearTable();
        voteDAO.clearTable();
    }
}
