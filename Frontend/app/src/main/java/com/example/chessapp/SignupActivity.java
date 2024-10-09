package com.example.chessapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignupActivity extends AppCompatActivity {

    private EditText usernameEditText;  // define username edittext variable
    private EditText passwordEditText;  // define password edittext variable
    private EditText confirmEditText;   // define confirm edittext variable
    private Button loginButton;         // define login button variable
    private Button signupButton;        // define signup button variable
    private ApiService apiService;      // declare ApiService for Retrofit

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        /* initialize UI elements */
        usernameEditText = findViewById(R.id.signup_username_edt);  // link to username edittext in the Signup activity XML
        passwordEditText = findViewById(R.id.signup_password_edt);  // link to password edittext in the Signup activity XML
        confirmEditText = findViewById(R.id.signup_confirm_edt);    // link to confirm edittext in the Signup activity XML
        loginButton = findViewById(R.id.signup_login_btn);    // link to login button in the Signup activity XML
        signupButton = findViewById(R.id.signup_signup_btn);  // link to signup button in the Signup activity XML

        // Initialize Retrofit with the backend API URL
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://coms-3090-050.class.las.iastate.edu/")  // Ensure it starts with 'http://' and ends with '/'
                .addConverterFactory(GsonConverterFactory.create())  // Convert JSON responses to Java objects
                .build();

        // Initialize API service
        apiService = retrofit.create(ApiService.class);

        /* click listener on login button pressed */
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Switch to Login Activity
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);  // Go to LoginActivity
            }
        });

        /* click listener on signup button pressed */
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* grab strings from user inputs */
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String confirm = confirmEditText.getText().toString();

                // Validate password and confirmation match
                if (password.equals(confirm)) {
                    Toast.makeText(getApplicationContext(), "Signing up...", Toast.LENGTH_LONG).show();

                    // Create the request body (UserRequest object)
                    UserRequest userRequest = new UserRequest(username, password);

                    // API call using Retrofit
                    apiService.signupUser(userRequest).enqueue(new Callback<UserResponse>() {
                        @Override
                        public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                            if (response.isSuccessful()) {
                                UserResponse userResponse = response.body();
                                if (userResponse != null) {
                                    // Display success message
                                    Toast.makeText(SignupActivity.this, "Signup successful! Welcome, " + username, Toast.LENGTH_LONG).show();

                                    // navigate to the next activity after signup
                                    Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }
                            } else {
                                // error response
                                Toast.makeText(SignupActivity.this, "Signup failed: " + response.message(), Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<UserResponse> call, Throwable t) {
                            // network failure
                            Toast.makeText(SignupActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

                } else {
                    // error message if passwords do not match
                    Toast.makeText(getApplicationContext(), "Passwords don't match", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
