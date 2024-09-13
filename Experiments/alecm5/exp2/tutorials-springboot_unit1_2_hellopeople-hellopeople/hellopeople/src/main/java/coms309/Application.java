package coms309;

import coms309.people.PeopleController;
import coms309.people.Person;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Sample Spring Boot Application.
 * 
 * @author Vivek Bengre
 */

@SpringBootApplication
public class Application {
	
    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
        PeopleController PC = new PeopleController();
        Person New1 = new Person("Alec", "Moore", "address", "555");
        System.out.println(PC.createPerson(New1));
    }

}
