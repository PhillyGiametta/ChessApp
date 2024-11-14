package Backend.ChessApp.Signup;

import Backend.ChessApp.Leaderboard.LeaderboardService;
import Backend.ChessApp.Leaderboard.LeaderboardEntry;
import Backend.ChessApp.Leaderboard.LeaderboardRepository;
import Backend.ChessApp.Users.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Signup", description = "Signup related HTTP methods")
public class SignupController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LeaderboardRepository leaderboardRepository;

    @Autowired
    private LeaderboardService leaderboardService;

    //when user signs up
    @Operation(summary = "Creates new user and adds user to database")
    @PostMapping(path = "/signup")
    String createUser(@RequestBody User user){
        if(user == null){
            return "Signup failed";
        }
        String name = user.getUserName();
        if(userRepository.findByUserName(name) != null){
            return "username already taken, please select a new username";
        }
        user = new User(user.getUserName(), user.getUserEmail() ,user.getUserPassword());
        userRepository.save(user);

        LeaderboardEntry leaderboardEntry = new LeaderboardEntry(user);
        leaderboardRepository.save(leaderboardEntry);
        leaderboardService.updateRankings();
        return "Successfully signed up";
    }

}
