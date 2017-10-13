package Database.Controllers;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/post")
@RestController
public class PostController {

    @RequestMapping(value = "/{id}/details", method = RequestMethod.POST)
    public String changePost(@PathVariable(name = "id") int id) {
        return "id "+id+" changed";
    }

    @RequestMapping(value = "/{id}/details", method = RequestMethod.GET)
    public String getPost(@PathVariable(name = "id") int id) {
        return "id "+id+" ...details";
    }

}
