package Database.Controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/forum")
@RestController
public class ForumController {

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String createForum() {
        return "Forum created";
    }
}
