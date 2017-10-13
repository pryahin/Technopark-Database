package Database.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/user")
@RestController
public class UserController {

    @RequestMapping(value = "/{nickname}/create", method = RequestMethod.POST)
    public ResponseEntity createUser(@PathVariable(name = "nickname") String nickname) {
        return ResponseEntity.status(HttpStatus.CREATED).body(nickname+" created");
    }

    @RequestMapping(value = "/{nickname}/profile", method = RequestMethod.GET)
    public ResponseEntity getUser(@PathVariable(name = "nickname") String nickname) {
        return ResponseEntity.status(HttpStatus.OK).body("info about " + nickname);
    }

    @RequestMapping(value = "/{nickname}/profile", method = RequestMethod.POST)
    public ResponseEntity changeUser(@PathVariable(name = "nickname") String nickname) {
        return ResponseEntity.status(HttpStatus.OK).body(nickname+" changed");
    }

}
