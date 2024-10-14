package Backend.ChessApp.Signup;

import Backend.ChessApp.Leaderboard.LeaderBoardService;
import Backend.ChessApp.Leaderboard.LeaderboardEntry;
import Backend.ChessApp.Leaderboard.LeaderboardRepository;
import Backend.ChessApp.Users.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SignupController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LeaderboardRepository leaderboardRepository;

    @Autowired
    private LeaderBoardService leaderBoardService;

    //when user signs up
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
        leaderBoardService.updateRankings();
        return "Successfully signed up";
    }

}
