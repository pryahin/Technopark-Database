package Database.Controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/service")
@RestController
public class ServiceController {

    @RequestMapping(value = "/clear", method = RequestMethod.POST)
    public String clearDB() {
        return "...clear...";
    }

    @RequestMapping(value = "/status", method = RequestMethod.GET)
    public String statusDB() {
        return "...status...";
    }

}