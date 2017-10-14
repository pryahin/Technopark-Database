package Database.Controllers;

import Database.DAO.UserDAO;
import Database.Models.ErrorModel;
import Database.Models.UserModel;
import Database.Models.UserUpdateModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/user")
@RestController
public class UserController {

    private UserDAO userDAO;

    @Autowired
    public UserController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @RequestMapping(value = "/{nickname}/create", method = RequestMethod.POST)
    public ResponseEntity createUser(@PathVariable(name = "nickname") String nickname, @RequestBody UserModel user) {
        user.setNickname(nickname);
        try {
            userDAO.addUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (DuplicateKeyException ex) {
            List<UserModel> users = userDAO.getUsers(nickname, user.getEmail());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(users);
        }
    }

    @RequestMapping(value = "/{nickname}/profile", method = RequestMethod.GET)
    public ResponseEntity getUser(@PathVariable(name = "nickname") String nickname) {
        UserModel user = userDAO.getUser(nickname);
        if (user == null) {
            ErrorModel error = new ErrorModel();
            error.setMessage("Can't find user with nickname " + nickname);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @RequestMapping(value = "/{nickname}/profile", method = RequestMethod.POST)
    public ResponseEntity changeUser(@PathVariable(name = "nickname") String nickname, @RequestBody UserUpdateModel user) {

        if (userDAO.getUser(nickname) == null) {
            ErrorModel error = new ErrorModel();
            error.setMessage("Can't find user with nickname " + nickname);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        try {
            userDAO.updateUser(nickname, user);
            return ResponseEntity.status(HttpStatus.OK).body(userDAO.getUser(nickname));
        } catch (DuplicateKeyException ex) {
            ErrorModel error = new ErrorModel();
            error.setMessage("New user profile data conflicts with existing users");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }
    }

}
