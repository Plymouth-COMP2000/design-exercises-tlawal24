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
        LinearLayout notificationsCard = findViewById(R.id.notificationsCard);

        manageAvailabilityCard.setOnClickListener(v -> {
            startActivity(new Intent(TrainerHomeActivity.this, ManageAvailabilityActivity.class));
        });

        myBookingsCard.setOnClickListener(v -> {
            startActivity(new Intent(TrainerHomeActivity.this, TrainerSessionsActivity.class));
        });

        notificationsCard.setOnClickListener(v -> {
            startActivity(new Intent(this, NotificationsActivity.class));
        });

        TextView logoutBtn = findViewById(R.id.logoutBtn);
        logoutBtn.setOnClickListener(v -> {
            prefs.edit().clear().apply();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}