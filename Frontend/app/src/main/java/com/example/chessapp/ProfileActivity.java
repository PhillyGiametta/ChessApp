package com.example.chessapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText emailEditText;
    private EditText passwordEditText; // Added password field
    private Button createButton;
    private Button updateButton;
    private Button deleteButton;
    private Button listButton;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize UI elements
        usernameEditText = findViewById(R.id.profile_username_edt);
        emailEditText = findViewById(R.id.profile_email_edt);
        passwordEditText = findViewById(R.id.profile_password_edt); // Initialize password field
        createButton = findViewById(R.id.profile_create_btn);
        updateButton = findViewById(R.id.profile_update_btn);
        deleteButton = findViewById(R.id.profile_delete_btn);
        listButton = findViewById(R.id.profile_list_btn);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        // Initialize Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.90.73.46:8080/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        apiService = retrofit.create(ApiService.class);

        // Button click listeners
        createButton.setOnClickListener(v -> createProfile());
        updateButton.setOnClickListener(v -> updateProfile());
        deleteButton.setOnClickListener(v -> deleteProfile());
        listButton.setOnClickListener(v -> listProfiles());

        // Load profile if username is passed
        //loadProfile();
    }

    // Load profile from the backend if username is passed
    private void loadProfile() {
        String username = "JohnDoe";
        if (username != null) {
            apiService.getProfile(username).enqueue(new Callback<UserResponse>() {
                @Override
                public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        UserResponse user = response.body();
                        usernameEditText.setText(user.getUsername());
                        emailEditText.setText(user.getEmail());
                    } else {
                        Toast.makeText(ProfileActivity.this, "Error loading profile: " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<UserResponse> call, Throwable t) {
                    Toast.makeText(ProfileActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // Create profile function with validation
    private void createProfile() {
        String username = usernameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim(); // Collect password

        // Validate inputs
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        UserRequest newUser = new UserRequest(username, email, password);

        apiService.createProfile(newUser).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ProfileActivity.this, "Profile created!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProfileActivity.this, "Profile creation failed: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Update profile function with validation
    private void updateProfile() {
        String username = usernameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim(); // Collect password
        System.out.println(username);

        // Validate inputs
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        UserRequest updatedUser = new UserRequest(username, email, password);

        apiService.updateProfile(username, updatedUser).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ProfileActivity.this, "Profile updated!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProfileActivity.this, "Update failed: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Delete profile function
    private void deleteProfile() {
        String username = usernameEditText.getText().toString();

        // First, get the user ID by username
        apiService.getProfile(username).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int userId = response.body().getId(); // Get the user ID

                    // Now delete the user by ID
                    apiService.deleteProfile(userId).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(ProfileActivity.this, "Profile deleted!", Toast.LENGTH_SHORT).show();
                                finish(); // Close the activity after deletion
                            } else {
                                Toast.makeText(ProfileActivity.this, "Delete failed: " + response.message(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(ProfileActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(ProfileActivity.this, "Failed to fetch user ID for deletion", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // List all profiles function
    private void listProfiles() {
        apiService.listProfiles().enqueue(new Callback<List<UserResponse>>() {
            @Override
            public void onResponse(Call<List<UserResponse>> call, Response<List<UserResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<UserResponse> profiles = response.body();
                    for (UserResponse profile : profiles) {
                        Log.d("Profile", "User: " + profile.getUsername() + ", Email: " + profile.getEmail());
                    }
                } else {
                    Toast.makeText(ProfileActivity.this, "Failed to load profiles: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<UserResponse>> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
