package Backend.ChessApp.Group;

import Backend.ChessApp.Users.User;

import java.util.ArrayList;

public class Group {
    private final int GroupId;
    private ArrayList<User> users = new ArrayList<>();
    private boolean full = false;

    public Group(int GroupId){
        this.GroupId = GroupId;
    }

    public boolean addUser(User user){
        if(full){
            return false;
        }

        users.add(user);
        if(users.size() >= 4){
            full = true;
        }
        return true;
    }

    public void removeUser(User user){
        users.remove(user);
        full = false;
    }
}