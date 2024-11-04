package Backend.ChessApp.Group;

import Backend.ChessApp.Users.User;
import jakarta.persistence.*;

import static jakarta.persistence.GenerationType.IDENTITY;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "group", schema = "DBChessApp")
public class Group {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "group_id", nullable = false)
    private int id;
    private String name;
    private boolean isFull;

    @OneToMany
    @JoinTable
    List<User> users;

    public Group(){
        //NULL Group
    }

    //Constructor
    public Group(String name){
        this.name = name;
    }

    //Getters and Setters
    public int getGroupId(){
        return this.id;
    }

    public void setGroupId(int id){
        this.id = id;
    }

    public boolean isFull(){
        return isFull;
    }

    public boolean addUser(User user){
        if(isFull){
            return false;
        }

        users.add(user);
        if(users.size() >= 4){
            isFull = true;
        }
        return true;
    }

    public void removeUser(User user){
        users.remove(user);
        isFull = false;
    }

    public List<User> getUsers(){
        return users;
    }
}