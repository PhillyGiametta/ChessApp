package Backend.UserProfile;
import Backend.UserProfile.Users.*;

import Backend.UserProfile.Users.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
@EnableAutoConfiguration
public class UserProfileApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserProfileApplication.class, args);
	}



	@Bean
	CommandLineRunner initUser(UserRepository userRepository){
		return args -> {
			User user1 = new User("Alec", "alecm5@iastate.edu", "urmom");
			User user2 = new User("Guest", "guest@fakeemail.com", "password");
			userRepository.save(user2);
			userRepository.save(user1);
		};
	}
}
