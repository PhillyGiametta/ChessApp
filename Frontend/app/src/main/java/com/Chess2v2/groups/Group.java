package com.Chess2v2.groups;

import com.Chess2v2.app.UserData;

public class Group {
    String groupName;
    int id;

    UserData[] users;

    public Group(String groupName){
        this.groupName = groupName;
    }

    public Group(String groupName, UserData[] users){
        this.groupName = groupName;
        this.users = users;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserData[] getUsers() {
        return users;
    }

    public void setUsers(UserData[] users) {
        this.users = users;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getPlayerCount() {
        return users == null ? 0 : users.length;
    }
}
