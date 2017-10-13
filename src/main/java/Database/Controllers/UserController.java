package Database.Controllers;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/user")
@RestController
public class UserController {

    @RequestMapping(value = "/{nickname}/create", method = RequestMethod.POST)
    public String createUser(@PathVariable(name = "nickname") String nickname) {
        return nickname+" created";
    }

    @RequestMapping(value = "/{nickname}/profile", method = RequestMethod.GET)
    public String getUser(@PathVariable(name = "nickname") String nickname) {
        return "info about " + nickname;
    }

    @RequestMapping(value = "/{nickname}/profile", method = RequestMethod.POST)
    public String changeUser(@PathVariable(name = "nickname") String nickname) {
        return nickname+" changed";
    }

}
