package com.example.session_5;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class Display extends AppCompatActivity {
    private TextView helloTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        helloTextView = findViewById(R.id.helloTextView);

        String username = getIntent().getStringExtra("username");
        helloTextView.setText("Hello again, " + username);
    }
}
