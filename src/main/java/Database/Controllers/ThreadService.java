package Database.Controllers;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/thread/")
@RestController
public class ThreadService {

    @RequestMapping(value = "/{slug_or_id}/create", method = RequestMethod.POST)
    public String createPost(@PathVariable(name="slug_or_id") String slug) {
        return "Post in "+slug+" created";
    }

    @RequestMapping(value = "/{slug_or_id}/details", method = RequestMethod.GET)
    public String getThread(@PathVariable(name="slug_or_id") String slug) {
        return "get "+slug+" thread";
    }

    @RequestMapping(value = "/{slug_or_id}/details", method = RequestMethod.POST)
    public String changeThread(@PathVariable(name="slug_or_id") String slug) {
        return "changed "+slug;
    }

    @RequestMapping(value = "/{slug_or_id}/posts", method = RequestMethod.GET)
    public String getPosts(@PathVariable(name="slug_or_id") String slug) {
        return "[Posts] from "+slug;
    }

    @RequestMapping(value = "/{slug_or_id}/vote", method = RequestMethod.POST)
    public String vote(@PathVariable(name="slug_or_id") String slug) {
        return "vote "+slug;
    }
}
