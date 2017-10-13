package Database.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/service")
@RestController
public class ServiceController {

    @RequestMapping(value = "/clear", method = RequestMethod.POST)
    public ResponseEntity clearDB() {
        return ResponseEntity.status(HttpStatus.OK).body("...clear...");
    }

    @RequestMapping(value = "/status", method = RequestMethod.GET)
    public ResponseEntity statusDB() {
        return ResponseEntity.status(HttpStatus.OK).body("...status...");
    }

}