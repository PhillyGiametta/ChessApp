package com.example.chessapp;

import android.os.Bundle;
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

public class ProfileActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText emailEditText;
    private Button updateButton;
    private Button deleteButton;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile); // Ensure this matches your layout file name

        // Initialize UI elements
        usernameEditText = findViewById(R.id.profile_username_edt);
        emailEditText = findViewById(R.id.profile_email_edt);
        updateButton = findViewById(R.id.profile_update_btn);
        deleteButton = findViewById(R.id.profile_delete_btn);

        // Initialize Retrofit
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://coms-3090-050.class.las.iastate.edu:8080/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        // Initialize API service
        apiService = retrofit.create(ApiService.class);

        // Fetch the profile details
        loadProfile();

        // Handle profile updates
        updateButton.setOnClickListener(v -> updateProfile());

        // Handle profile deletion
        deleteButton.setOnClickListener(v -> deleteProfile());
    }

    // Load profile function
    private void loadProfile() {
        String username = getIntent().getStringExtra("USERNAME");
        apiService.getProfile(username).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserResponse user = response.body();
                    usernameEditText.setText(user.getUsername());
                    emailEditText.setText(user.getEmail()); // Assuming the backend sends email too
                } else {
                    Toast.makeText(ProfileActivity.this, "Error loading profile", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Update profile function
    private void updateProfile() {
        String username = usernameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        UserRequest updatedUser = new UserRequest(username, email);
        apiService.updateProfile(updatedUser).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ProfileActivity.this, "Profile updated!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProfileActivity.this, "Update failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Delete profile function
    private void deleteProfile() {
        String username = usernameEditText.getText().toString();
        apiService.deleteProfile(username).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ProfileActivity.this, "Profile deleted!", Toast.LENGTH_SHORT).show();
                    finish(); // Close the activity
                } else {
                    Toast.makeText(ProfileActivity.this, "Delete failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
