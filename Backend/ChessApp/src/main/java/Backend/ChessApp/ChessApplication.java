package Backend.ChessApp;

import Backend.ChessApp.Users.*;
import jakarta.transaction.Transactional;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;

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
	@Bean
	@Transactional
	CommandLineRunner deleteBadUser(UserRepository userRepo) {
		return args -> {
			List<User> users = userRepo.findAll();
			for (User user : users) {
				if (user.getUserEmail() == null ||
						user.getUserPassword() == null ||
						user.getUserName() == null) {
					userRepo.delete(user);
				}
			}
		};
	}
}
