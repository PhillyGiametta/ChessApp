package Backend.ChessApp.Group;

import Backend.ChessApp.AdminControl.Admin;
import Backend.ChessApp.Users.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;

import static jakarta.persistence.GenerationType.IDENTITY;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Entity
@Table(name = "groups", schema = "DBChessApp")
public class Group {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "group_id", nullable = false)
    private int id;

    private String groupName;
    private boolean isFull;
    private String joinCode;

    @OneToMany(mappedBy = "group",fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<User> users = new ArrayList<>();

    @OneToOne
    private Admin admin;

    public Group(){
        //NULL Group
    }

    //Constructor
    public Group(String groupName){
        this.groupName = groupName;
        this.users = new ArrayList<>();
        joinCode = "";
        generateJoinCode();
    }

    //Getters and Setters
    public int getGroupId(){
        return this.id;
    }

    public Admin getAdminId(){
        return admin;
    }

    public void setAdmin(Admin admin){
        this.admin = admin;
    }

    public boolean isAdmin(User user){
        return admin!= null && admin.getUser().equals(user);
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
        if(admin == null){
            admin = new Admin();
            admin.setUser(user);
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
        if(admin.getUser() == user && !users.isEmpty()){
            admin.setUser(users.get(0));
        }else if(users.isEmpty()){
            admin = null;
        }
    }

    public List<User> getUsers(){
        return users;
    }

    public String getGroupName(){
        return groupName;
    }

    public String getJoinCode(){
        return joinCode;
    }

    public void setJoinCode(String joinCode){
        this.joinCode = joinCode;
    }

    private void generateJoinCode(){
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        Random r = new Random();
        while(joinCode.length() < 5){
            int index = (int) (r.nextFloat() * chars.length());
            joinCode += chars.charAt(index);
        }
    }
}