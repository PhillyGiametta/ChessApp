package com.Chess2v2.groups;

import com.Chess2v2.app.UserData;

public class Group {
    String groupName;

    UserData[] users;

    public Group(String groupName){
        this.groupName = groupName;
    }

    public Group(String groupName, UserData[] users){
        this.groupName = groupName;
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
