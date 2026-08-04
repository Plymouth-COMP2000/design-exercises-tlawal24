package com.example.gymfitnessapplication;

import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Bookasession extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bookasession);

        // Create NotificationDao
        NotificationDao notificationDao = new NotificationDao(this);

        // Get logged-in username
        String username = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
                .getString("username", "unknown_user");

        // Get trainer name passed from previous screen
        String trainerName = getIntent().getStringExtra("trainerName");

        Button confirmButton = findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(v -> {

            // Trigger notification BEFORE navigating
            notificationDao.addNotificationIfEnabled(username, "booking",
                    "Your booking with " + trainerName + " was confirmed.");

            // Optional feedback
            Toast.makeText(this, "Booking confirmed!", Toast.LENGTH_SHORT).show();

            // Navigate to confirmation screen (only once)
            Intent intent = new Intent(this, Confirmation.class);
            startActivity(intent);
        });


    }
}
