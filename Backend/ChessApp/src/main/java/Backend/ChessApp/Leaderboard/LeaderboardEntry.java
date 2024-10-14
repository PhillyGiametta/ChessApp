package Backend.ChessApp.Leaderboard;

import Backend.ChessApp.Users.User;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "leaderboard", schema = "DBChessApp")
public class LeaderboardEntry {

    //CLASS VARIABLES-------------------------------------------

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "leaderboard_entry_id", nullable = false)
    private int id;
    private int wins;
    private int losses;
    private double winLossRatio;
    private int rankPosition;
    private int rating;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public LeaderboardEntry(){

    }
    public LeaderboardEntry(User user) {
        this.user = user;
        rankPosition = 0;
        rating = 0;
        wins = 0;
        losses = 0;
        winLossRatio = 0;
    }

    public void calculateWinLossRatio(){
        if(losses == 0){
            winLossRatio = wins;
        }else{
            winLossRatio = (double) (wins / losses);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRankPosition() {
        return rankPosition;
    }

    public void setRankPosition(int rankPosition) {
        this.rankPosition = rankPosition;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getUserWins() {
        return wins;
    }

    public void setUserWins(int wins) {
        this.wins = wins;
    }

    public int getUserLosses() {
        return losses;
    }

    public void setUserLosses(int losses) {
        this.losses = losses;
    }

    public double getUserWLRatio() {
        return winLossRatio;
    }

    public void setUserWLRatio(double winLossRatio) {
        this.winLossRatio = winLossRatio;
    }

}
