package com.example.gymfitnessapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TrainerHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_trainer_home);

        SharedPreferences prefs = getSharedPreferences("gym_prefs", MODE_PRIVATE);
        String trainerUsername = prefs.getString("logged_in_user", "trainer");

        TextView trainerNameText = findViewById(R.id.trainerNameText);
        trainerNameText.setText(trainerUsername);

        LinearLayout manageAvailabilityCard = findViewById(R.id.manageAvailabilityCard);
        LinearLayout myBookingsCard = findViewById(R.id.myBookingsCard);

        manageAvailabilityCard.setOnClickListener(v -> {
            Intent intent = new Intent(TrainerHomeActivity.this, ManageAvailabilityActivity.class);
            startActivity(intent);
        });

        myBookingsCard.setOnClickListener(v -> {
            Intent intent = new Intent(TrainerHomeActivity.this, TrainerSessionsActivity.class);
            startActivity(intent);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}