package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;

public class CounterActivity extends AppCompatActivity {

    private TextView numberTxt;       // define number TextView variable
    private TextView actionMessageTxt; // define message TextView for Increase/Decrease
    private Button increaseBtn;       // define increase Button variable
    private Button decreaseBtn;       // define decrease Button variable
    private Button backBtn;           // define back Button variable

    private int counter = 0;          // counter variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);

        /* initialize UI elements */
        numberTxt = findViewById(R.id.number);
        actionMessageTxt = findViewById(R.id.action_message_txt); // link to the new TextView
        increaseBtn = findViewById(R.id.counter_increase_btn);
        decreaseBtn = findViewById(R.id.counter_decrease_btn);
        backBtn = findViewById(R.id.counter_back_btn);

        /* when increase button is pressed */
        increaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberTxt.setText(String.valueOf(++counter));
                actionMessageTxt.setText("Increase!"); // Display "Increase!" message
            }
        });

        /* when decrease button is pressed */
        decreaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberTxt.setText(String.valueOf(--counter));
                actionMessageTxt.setText("Decrease!"); // Display "Decrease!" message
            }
        });

        /* when back button is pressed, switch back to MainActivity */
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CounterActivity.this, MainActivity.class);
                intent.putExtra("NUM", String.valueOf(counter));  // key-value to pass to MainActivity
                startActivity(intent);
            }
        });
    }
}
