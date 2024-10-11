package Backend.ChessApp.Leaderboard;

import Backend.ChessApp.Users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeaderboardRepository extends JpaRepository<Leaderboard, Integer> {
    Leaderboard findById(int id);

//    List<Leaderboard> findAllByRankPosition();
}
