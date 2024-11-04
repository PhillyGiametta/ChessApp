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

}