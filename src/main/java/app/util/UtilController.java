package app.util;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UtilController {
    @RequestMapping(value = "/ping", method = RequestMethod.GET)
    public String generateFromArray() {
        System.out.println("request come");
        return "ping success";
    }
}
