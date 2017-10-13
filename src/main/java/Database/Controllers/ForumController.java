package Database.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/forum")
@RestController
public class ForumController {

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity createForum() {
        return ResponseEntity.status(HttpStatus.CREATED).body("Forum created");
    }

    @RequestMapping(value = "/{slug}/create", method = RequestMethod.POST)
    public ResponseEntity createThread(@PathVariable(name = "slug") String slug) {
        return ResponseEntity.status(HttpStatus.CREATED).body(slug+" - created");
    }

    @RequestMapping(value = "{slug}/details", method = RequestMethod.GET)
    public ResponseEntity getDetails(@PathVariable(name = "slug") String slug) {
        return ResponseEntity.status(HttpStatus.OK).body(slug+": ...{details}");
    }

    @RequestMapping(value = "{slug}/threads", method = RequestMethod.GET)
    public ResponseEntity getThreads(@PathVariable(name = "slug") String slug) {
        return ResponseEntity.status(HttpStatus.OK).body(slug+": ...{threads}");
    }

    @RequestMapping(value = "{slug}/users", method = RequestMethod.GET)
    public ResponseEntity getUsers(@PathVariable(name = "slug") String slug) {
        return ResponseEntity.status(HttpStatus.OK).body(slug+" - users: ...");
    }
}
