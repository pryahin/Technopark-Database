package Database.Controllers;

import org.springframework.web.bind.annotation.PathVariable;
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

    @RequestMapping(value = "/{slug}/create", method = RequestMethod.POST)
    public String slugCreate(@PathVariable(name = "slug") String slug) {
        return slug+" - created";
    }

    @RequestMapping(value = "{slug}/details", method = RequestMethod.GET)
    public String slugDetails(@PathVariable(name = "slug") String slug) {
        return slug+": ...{details}";
    }

    @RequestMapping(value = "{slug}/users", method = RequestMethod.GET)
    public String slugUsers(@PathVariable(name = "slug") String slug) {
        return slug+" - users: ...";
    }
}
