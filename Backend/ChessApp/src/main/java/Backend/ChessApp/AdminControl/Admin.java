package Backend.ChessApp.AdminControl;


import Backend.ChessApp.Game.ChessGame;
import Backend.ChessApp.Group.Group;
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
    @Column(name = "admin_id")
    private int id;


    @OneToOne
    @JoinColumn(name="user_id") // join with user_id
    private User user;

//    @OneToOne
//    @JoinColumn(name = "game_id") // join with game_id
//    ChessGame chessGame;

    @OneToOne
    @JoinColumn(name = "group_id")
    private Group group;


//    private List<User> usersInGame;

    public Admin(User user){
        this.user = user;
    }

    public Admin(){

    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public Group getGroup() {
        return group;
    }
    public void setGroup(Group group) {
        this.group = group;
    }


}
