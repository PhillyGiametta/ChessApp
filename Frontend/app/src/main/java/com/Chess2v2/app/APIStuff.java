package com.Chess2v2.app;

import com.Chess2v2.groups.Group;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface  APIStuff {
    @GET("/users")
    Call<List<UserData>> listProfiles();

    @GET("/groups")
    Call<List<Group>> listGroups();

    @GET("/groups/{groupName}")
    Call<Group> getGroup(@Path("groupName") String groupName);
}
