package Backend.ChessApp.AdminControl;


import Backend.ChessApp.Group.Group;
import Backend.ChessApp.Users.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
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

    @OneToOne(mappedBy = "admin", orphanRemoval = true)
    @JsonBackReference
    private Group group;

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
