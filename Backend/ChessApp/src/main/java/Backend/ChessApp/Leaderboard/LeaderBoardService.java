package Backend.ChessApp.Leaderboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class LeaderBoardService {

    @Autowired
    private LeaderboardRepository leaderboardRepository;

    public LeaderBoardService() {}

    public List<LeaderboardEntry> updateRankings(){
        List<LeaderboardEntry> leaderboardEntries = leaderboardRepository.findAll();

        leaderboardEntries.sort(new SortById());


        for(int i = 0; i < leaderboardEntries.size(); i++){
            leaderboardEntries.get(i).setRankPosition(i + 1);

            leaderboardRepository.save(leaderboardEntries.get(i));
        }
        return leaderboardEntries;
    }
}

class SortById implements Comparator<LeaderboardEntry>{

    @Override
    public int compare(LeaderboardEntry a, LeaderboardEntry b) {
        return Integer.compare(b.getRating(), a.getRating());
    }
}
