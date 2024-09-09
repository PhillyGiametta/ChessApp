package coms309;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
class WelcomeController {

    String[] itemList = {"item 1", "item 2", "item 3", "item 4"};

    @GetMapping("/")
    public String welcome() {
        return "Hello and welcome to COMS 309";
    }

    @GetMapping("/{name}")
    public String welcome(@PathVariable String name) {
        return "Hello and welcome to COMS 309: " + name;
    }

    @GetMapping("/smile")
    public String joke() {
        return ":)";
    }

    @GetMapping("/list")
    public String list(){
        String result = "";
        for(String s : itemList){
            result += s + "\n";
        }
        return result;
    }
}
