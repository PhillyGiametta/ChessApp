package Backend.ChessApp.AdminControl;


import Backend.ChessApp.Game.ChessGame;
import Backend.ChessApp.Users.User;
import Backend.ChessApp.Users.UserRepository;
import jakarta.persistence.*;
import org.apache.catalina.realm.UserDatabaseRealm;

import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "admin", schema = "DBChessApp")
public class Admin {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "admin_id", nullable = false)
    private int id;


    @OneToOne
    @JoinColumn(name="user_id")
    User user;

    @OneToOne
    ChessGame chessGame;

    @OneToMany
    private List<User> usersInGame;

    public Admin(User user){
        this.user = user;
    }

}
