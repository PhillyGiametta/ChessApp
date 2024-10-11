package Backend.ChessApp;

import Backend.ChessApp.Users.*;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class ChessApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChessApplication.class, args);
	}



	@Bean
	CommandLineRunner initUser(UserRepository userRepository){
		return args -> {
			User user1 = new User("Alec", "alecm5@iastate.edu", "urmom");
			User user2 = new User("Guest", "guest@fakeemail.com", "password");
			if(!(userRepository.existsByUserEmail(user1.getUserEmail()) || userRepository.existsByUserEmail(user2.getUserEmail()))) {
				userRepository.save(user2);
				userRepository.save(user1);
			}
		};
	}
}
