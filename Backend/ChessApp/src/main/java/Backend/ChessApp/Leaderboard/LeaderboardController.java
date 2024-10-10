package Backend.ChessApp.Leaderboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LeaderboardController {

    @Autowired
    private LeaderboardRepository leaderboardRepository;

    //Create
    @PostMapping(path = "/")
    public Leaderboard createLeaderboardEntry(Leaderboard leaderboard){
        return leaderboardRepository.save(leaderboard);
    }
}
