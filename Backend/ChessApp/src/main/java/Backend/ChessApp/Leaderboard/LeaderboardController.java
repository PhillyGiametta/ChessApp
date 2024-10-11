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

    //List
    @GetMapping(path = "/leaderboard")
    public List<Leaderboard> getLeaderboard() {
        return leaderboardRepository.findAll();
    }

    //Get
    @GetMapping(path = "/leaderboard/{id}")
    public Leaderboard getLeaderboard(@PathVariable int id) {
        return leaderboardRepository.findById(id);
    }

    //Create
    @PostMapping(path = "/leaderboard")
    public Leaderboard createLeaderboardEntry(@RequestBody Leaderboard leaderboard){
        if(leaderboard == null){
            return null;
        }
        return leaderboardRepository.save(leaderboard);
    }

    //Update
    @PutMapping(path = "/leaderboard/{id}")
    public Leaderboard updateLeaderboardEntry(@PathVariable int id, @RequestBody Leaderboard update){
        Leaderboard leaderboard = leaderboardRepository.findById(id);
        if(leaderboard == null){
            return null;
        }
        leaderboard.setRating(update.getRating());
        leaderboard.setRankPosition(update.getRankPosition());
        return leaderboardRepository.save(leaderboard);
    }

    //Delete
    @DeleteMapping(path = "/leaderboard/{id}")
    public String deleteLeaderboardEntry(@PathVariable int id){
        leaderboardRepository.deleteById(id);
        return "deleted entry";
    }
}
