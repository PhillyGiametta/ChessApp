package Backend.ChessApp.Leaderboard;

import Backend.ChessApp.Users.User;
import Backend.ChessApp.Users.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
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
    private LeaderboardService leaderboardService;
    //List
    @Operation(summary = "Fetches all leaderboard entries sorted by position on the leaderboard")
    @GetMapping(path = "/leaderboard")
    public List<LeaderboardEntry> getLeaderboard() {
        return leaderboardRepository.findAllByOrderByRankPositionAsc();
    }

    //Get
    @Operation(summary = "Fetches specific users leaderboard information")
    @GetMapping(path = "/leaderboard/{id}")
    public LeaderboardEntry getLeaderboard(@PathVariable int id) {
        return leaderboardRepository.findById(id);
    }

    //Create
    @Operation(summary = "Creates a leaderboard entry for a user given the userId in the request body")
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
        leaderboardService.updateRankings();
        return leaderboardEntry;
    }

    //Create
    @Operation(summary = "Creates a leaderboard entry for a user given the userId in the path")
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
        leaderboardService.updateRankings();
        return leaderboardEntry;
    }

    //Update
    @Operation(summary = "Updates a leaderboard entry for a user given the userId in the path")
    @PutMapping(path = "/leaderboard/{id}")
    public LeaderboardEntry updateLeaderboardEntry(@PathVariable int id, @RequestBody LeaderboardEntry update){
        LeaderboardEntry leaderboard = leaderboardRepository.findById(id);
        if(leaderboard == null){
            return null;
        }
        leaderboard.setRating(update.getRating());
        leaderboard.setRankPosition(update.getRankPosition());
        leaderboardRepository.save(leaderboard);
        leaderboardService.updateRankings();
        return leaderboard;
    }

    //Delete
    @Operation(summary = "Deletes a leaderboard entry for a user given the userId in the path")
    @DeleteMapping(path = "/leaderboard/{id}")
    public String deleteLeaderboardEntry(@PathVariable int id){
        leaderboardRepository.deleteById(id);
        leaderboardService.updateRankings();
        return "deleted entry";
    }
}
