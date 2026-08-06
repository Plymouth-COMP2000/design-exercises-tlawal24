package com.example.gymfitnessapplication;

import android.os.Bundle;

import android.content.Intent;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.SharedPreferences;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MemberHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_member_home);
        LinearLayout findTrainerCard = findViewById(R.id.findTrainerCard);
        LinearLayout mySessionsCard = findViewById(R.id.mySessionsCard);

        findTrainerCard.setOnClickListener(v -> {
            Intent intent = new Intent(MemberHomeActivity.this, TrainerBrowseActivity.class);
            startActivity(intent);
        });

        LinearLayout notificationsBtn = findViewById(R.id.notificationsBtn);
        notificationsBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, NotificationsActivity.class));
        });


        mySessionsCard.setOnClickListener(v -> {
            Intent intent = new Intent(MemberHomeActivity.this, MySessions.class);
            startActivity(intent);
        });

        LinearLayout accountCard = findViewById(R.id.accountCard);
        accountCard.setOnClickListener(v -> {
            startActivity(new Intent(this, AccountDetailsActivity.class));
        });

        TextView logoutBtn = findViewById(R.id.logoutBtn);
        logoutBtn.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("gym_prefs", MODE_PRIVATE);
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