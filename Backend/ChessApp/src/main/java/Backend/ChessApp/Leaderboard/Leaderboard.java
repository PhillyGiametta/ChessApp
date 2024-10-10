package Backend.ChessApp.Leaderboard;

import Backend.ChessApp.Users.User;
import jakarta.persistence.*;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "leaderboard", schema = "DBChessApp")
public class Leaderboard {

    //CLASS VARIABLES-------------------------------------------
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private int leaderboardId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private int rankPosition;
    private int rating;
    private
}
