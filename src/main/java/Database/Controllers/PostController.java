package Database.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/post")
@RestController
public class PostController {

    @RequestMapping(value = "/{id}/details", method = RequestMethod.POST)
    public ResponseEntity changePost(@PathVariable(name = "id") int id) {
        return ResponseEntity.status(HttpStatus.OK).body("id "+id+" changed");
    }

    @RequestMapping(value = "/{id}/details", method = RequestMethod.GET)
    public ResponseEntity getPost(@PathVariable(name = "id") int id) {
        return ResponseEntity.status(HttpStatus.OK).body("id "+id+" ...details");
    }

}
