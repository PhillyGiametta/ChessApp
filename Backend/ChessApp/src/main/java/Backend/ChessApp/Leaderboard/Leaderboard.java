package Backend.ChessApp.Leaderboard;

import Backend.ChessApp.Users.User;
import jakarta.persistence.*;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
//@Table(name = "leaderboard", schema = "DBChessApp")
public class Leaderboard {

    //CLASS VARIABLES-------------------------------------------
    @Id
    @GeneratedValue(strategy = IDENTITY)
//    @Column(name = "leaderboard_id")
    private int id;

    private int rankPosition;
    private int rating;

//    @OneToOne
//    @JoinColumn(name = "user_id")
//    private User user;

    public Leaderboard(){

    }
    public Leaderboard(int rankPosition, int rating) {
        this.rankPosition = rankPosition;
        this.rating = rating;
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

//    public User getUser() {
//        return user;
//    }
//
//    public void setUser(User user) {
//        this.user = user;
//    }

}
