package com.example.chessapp;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    // Sign Up and Login
    @POST("/signup")
    Call<ResponseBody> signupUser(@Body UserRequest userRequest);


    @POST("/login")
    Call<ResponseBody> loginUser(@Body UserRequest userRequest);

    @GET("/users/userName/{userName}")
    Call<UserResponse> getProfile(@Path("userName") String userName);

    @POST("/users")
    Call<UserResponse> createProfile(@Body UserRequest user);

    // Update a profile by username
    @PUT("/users/{userName}")
    Call<UserResponse> updateProfile(@Path("userName") String userName, @Body UserRequest user);

    // Delete profile by userId
    @DELETE("/users/{userId}")
    Call<Void> deleteProfile(@Path("userId") int userId);

    // Get all profiles
    @GET("/users")
    Call<List<UserResponse>> listProfiles();
}
