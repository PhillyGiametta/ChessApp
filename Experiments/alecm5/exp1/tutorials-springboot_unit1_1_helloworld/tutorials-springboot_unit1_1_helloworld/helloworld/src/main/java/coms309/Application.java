package coms309;

import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * Hello World Spring Boot Application.
 * 
 * @author Alec Moore
 */

@SpringBootApplication
public class Application {
    /**
     * This main function says Hello to me and welcomes to 309. Used for practice with git and the IDE IntelliJ, plus SpringBoot?.
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        WelcomeController wc = new WelcomeController();
        SpringApplication.run(Application.class, args);
        System.out.println(wc.welcome("Alec"));

    }

}
