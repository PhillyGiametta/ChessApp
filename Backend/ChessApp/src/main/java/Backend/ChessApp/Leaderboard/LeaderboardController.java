package Backend.ChessApp.Leaderboard;

import Backend.ChessApp.Users.User;
import Backend.ChessApp.Users.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LeaderboardController {



    @Autowired
    private LeaderboardRepository leaderboardRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LeaderBoardService leaderBoardService;
    //List
    @GetMapping(path = "/leaderboard")
    public List<LeaderboardEntry> getLeaderboard() {
        return leaderboardRepository.findAllByOrderByRankPositionAsc();
    }

    //Get
    @GetMapping(path = "/leaderboard/{id}")
    public LeaderboardEntry getLeaderboard(@PathVariable int id) {
        return leaderboardRepository.findById(id);
    }

    //Create
    @PostMapping(path = "/leaderboard")
    public LeaderboardEntry createLeaderboardEntry(@RequestBody LeaderboardEntry leaderboardEntry){
        if(leaderboardEntry == null){
            return null;
        }

        User user = userRepository.findById(leaderboardEntry.getUser().getUserId());
        if(user == null){
            return null;
        }
        leaderboardEntry.setUser(user);
        leaderboardRepository.save(leaderboardEntry);
        leaderBoardService.updateRankings();
        return leaderboardEntry;
    }

    //Create
    @PostMapping(path = "/leaderboard/{id}")
    public LeaderboardEntry createLeaderboardEntry(@RequestBody LeaderboardEntry leaderboardEntry, @PathVariable int id){
        if(leaderboardEntry == null){
            return null;
        }

        User user = userRepository.findById(id);
        if(user == null){
            return null;
        }
        leaderboardEntry.setUser(user);

        leaderboardRepository.save(leaderboardEntry);
        leaderBoardService.updateRankings();
        return leaderboardEntry;
    }

    //Update
    @PutMapping(path = "/leaderboard/{id}")
    public LeaderboardEntry updateLeaderboardEntry(@PathVariable int id, @RequestBody LeaderboardEntry update){
        LeaderboardEntry leaderboard = leaderboardRepository.findById(id);
        if(leaderboard == null){
            return null;
        }
        leaderboard.setRating(update.getRating());
        leaderboard.setRankPosition(update.getRankPosition());
        leaderboardRepository.save(leaderboard);
        leaderBoardService.updateRankings();
        return leaderboard;
    }

    //Delete
    @DeleteMapping(path = "/leaderboard/{id}")
    public String deleteLeaderboardEntry(@PathVariable int id){
        leaderboardRepository.deleteById(id);
        leaderBoardService.updateRankings();
        return "deleted entry";
    }
}
