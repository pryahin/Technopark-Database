package Database.Controllers;

import Database.DAO.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/service")
@RestController
public class ServiceController {

    private ServiceDAO serviceDAO;

    @Autowired
    public ServiceController(ServiceDAO serviceDAO) {
        this.serviceDAO = serviceDAO;
    }

    @RequestMapping(value = "/clear", method = RequestMethod.POST)
    public ResponseEntity clearDB() {
        serviceDAO.clearDB();
        return ResponseEntity.status(HttpStatus.OK).body("");
    }

    @RequestMapping(value = "/status", method = RequestMethod.GET)
    public ResponseEntity statusDB() {
        return ResponseEntity.status(HttpStatus.OK).body(serviceDAO.getStatus());
    }
}