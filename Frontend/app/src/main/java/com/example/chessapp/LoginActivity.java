package com.example.chessapp;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import android.widget.Toast;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button signupButton;
    private ApiService apiService;  // Declare ApiService for Retrofit

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        // Initialize UI elements
        usernameEditText = findViewById(R.id.login_username_edt);
        passwordEditText = findViewById(R.id.login_password_edt);
        loginButton = findViewById(R.id.login_login_btn);
        signupButton = findViewById(R.id.login_signup_btn);

        // Initialize Retrofit with the backend API URL and Gson converter
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://coms-3090-050.class.las.iastate.edu:8080/")
                .addConverterFactory(GsonConverterFactory.create(gson)) // Use lenient Gson
                .build();

        // Initialize API service
        apiService = retrofit.create(ApiService.class);

        // Set click listener on login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Grab strings from user inputs
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // Validate input fields are not empty
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter both username and password.", Toast.LENGTH_LONG).show();
                    return;
                }

                // Create the request body (UserRequest object)
                UserRequest userRequest = new UserRequest(username, password);

                // API call for login using Retrofit
                apiService.loginUser(userRequest).enqueue(new Callback<UserResponse>() {
                    @Override
                    public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            // Success: Navigate to the main activity
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("USERNAME", username);  // Pass username to MainActivity
                            startActivity(intent);
                        } else {
                            // Handle login failure (e.g., wrong credentials)
                            Toast.makeText(LoginActivity.this, "Login failed: " + response.message(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserResponse> call, Throwable t) {
                        // Network error or JSON parsing issue
                        Toast.makeText(LoginActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        // Set click listener on signup button
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Switch to Signup Activity
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }
}
