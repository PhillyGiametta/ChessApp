package Backend.ChessApp.Group;

import Backend.ChessApp.Users.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;

import static jakarta.persistence.GenerationType.IDENTITY;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "groups", schema = "DBChessApp")
public class Group {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "group_id", nullable = false)
    private int id;

    private String groupName;
    private boolean isFull;

    @OneToMany(mappedBy = "group",fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<User> users = new ArrayList<>();

    @OneToOne
    private User leader;

    public Group(){
        //NULL Group
    }

    //Constructor
    public Group(String groupName){
        this.groupName = groupName;
        this.users = new ArrayList<>();
    }

    //Getters and Setters
    public int getGroupId(){
        return this.id;
    }

    public User getLeaderId(){
        return leader;
    }

    public void setLeader(User user){
        this.leader = user;
    }

    public boolean isLeader(User user){
        return leader!= null && leader.equals(user);
    }

    public void setGroupId(int id){
        this.id = id;
    }

    public boolean isFull(){
        return isFull;
    }

    public boolean isEmpty(){
        return users.isEmpty();
    }

    @Transactional
    public boolean addUser(User user){
        if(isFull){
            return false;
        }

        users.add(user);
        user.setGroup(this);

        //Assign leader to first user who joins (the user who created the group)
        if(leader == null){
            leader = user;
        }

        if(users.size() >= 4){
            isFull = true;
        }
        System.out.println("users.size = " + users.size());
        return true;
    }

    @Transactional
    public void removeUser(User user){
        users.remove(user);
        user.setGroup(null);
        isFull = false;

        //Reassign leader if the leader leaves
        if(leader == user && !users.isEmpty()){
            leader = users.get(0);
        }else if(users.isEmpty()){
            leader = null;
        }
    }

    public List<User> getUsers(){
        return users;
    }

    public String getGroupName(){
        return groupName;
    }
}