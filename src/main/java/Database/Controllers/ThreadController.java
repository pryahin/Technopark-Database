package Database.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/thread/")
@RestController
public class ThreadController {

    @RequestMapping(value = "/{slug_or_id}/create", method = RequestMethod.POST)
    public ResponseEntity createPost(@PathVariable(name="slug_or_id") String slug) {
        return ResponseEntity.status(HttpStatus.CREATED).body("Post in "+slug+" created");
    }

    @RequestMapping(value = "/{slug_or_id}/details", method = RequestMethod.GET)
    public ResponseEntity getThread(@PathVariable(name="slug_or_id") String slug) {
        return ResponseEntity.status(HttpStatus.OK).body("get "+slug+" thread");
    }

    @RequestMapping(value = "/{slug_or_id}/details", method = RequestMethod.POST)
    public ResponseEntity changeThread(@PathVariable(name="slug_or_id") String slug) {
        return ResponseEntity.status(HttpStatus.OK).body("changed "+slug);
    }

    @RequestMapping(value = "/{slug_or_id}/posts", method = RequestMethod.GET)
    public ResponseEntity getPosts(@PathVariable(name="slug_or_id") String slug) {
        return ResponseEntity.status(HttpStatus.OK).body("[Posts] from "+slug);
    }

    @RequestMapping(value = "/{slug_or_id}/vote", method = RequestMethod.POST)
    public ResponseEntity vote(@PathVariable(name="slug_or_id") String slug) {
        return ResponseEntity.status(HttpStatus.OK).body("vote "+slug);
    }
}
