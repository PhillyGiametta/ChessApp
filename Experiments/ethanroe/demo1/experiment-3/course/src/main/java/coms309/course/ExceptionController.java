package coms309.course;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
class ExceptionController {
    @GetMapping("error")
    @RequestMapping(method = RequestMethod.GET, path = "/oops")
    public String triggerException() {
        return("Exception triggered");
    }

}
