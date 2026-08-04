package com.example.gymfitnessapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MySessions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_sessions);

        SharedPreferences prefs = getSharedPreferences("gym_prefs", MODE_PRIVATE);
        String memberUsername = prefs.getString("logged_in_user", "unknown_member");

        LinearLayout sessionsList = findViewById(R.id.sessionsList);
        loadSessions(memberUsername, sessionsList);

        TextView notifPrefs = findViewById(R.id.notifPrefs);
        notifPrefs.setOnClickListener(v -> {
            startActivity(new Intent(this, NotificationPreferencesActivity.class));
        });

        TextView homeNavItem = findViewById(R.id.homeNavItem);
        homeNavItem.setOnClickListener(v -> {
            Intent intent = new Intent(this, MemberHomeActivity.class);
            startActivity(intent);
            finish();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void loadSessions(String memberUsername, LinearLayout container) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query("sessions",
                new String[]{"trainer_username", "date", "time", "status", "booking_reference"},
                "member_username = ?",
                new String[]{memberUsername},
                null, null, "date, time");

        if (cursor.getCount() == 0) {
            TextView emptyText = new TextView(this);
            emptyText.setText("No sessions booked yet");
            emptyText.setTextColor(0xFF666666);
            emptyText.setGravity(Gravity.CENTER);
            emptyText.setPadding(0, 40, 0, 0);
            container.addView(emptyText);
        } else {
            while (cursor.moveToNext()) {
                String trainer = cursor.getString(cursor.getColumnIndexOrThrow("trainer_username"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                String time = cursor.getString(cursor.getColumnIndexOrThrow("time"));
                String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
                String reference = cursor.getString(cursor.getColumnIndexOrThrow("booking_reference"));

                container.addView(buildSessionCard(displayNameFor(trainer), date, time, status, reference));
            }
        }

        cursor.close();
        db.close();
    }

    private LinearLayout buildSessionCard(String trainerName, String date, String time, String status, String reference) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setBackgroundResource(android.R.drawable.dialog_holo_light_frame);
        card.setPadding(32, 32, 32, 32);

        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        cardParams.setMargins(0, 0, 0, 24);
        card.setLayoutParams(cardParams);

        TextView trainerText = new TextView(this);
        trainerText.setText(trainerName);
        trainerText.setTextSize(16);
        trainerText.setTypeface(null, android.graphics.Typeface.BOLD);

        TextView dateTimeText = new TextView(this);
        dateTimeText.setText(date + ", " + time);
        dateTimeText.setTextSize(12);
        dateTimeText.setTextColor(0xFF3C5AC8);

        TextView statusText = new TextView(this);
        statusText.setText("Status: " + status);
        statusText.setTextSize(12);
        statusText.setTextColor(0xFF666666);

        card.addView(trainerText);
        card.addView(dateTimeText);
        card.addView(statusText);

        return card;
    }

    private String displayNameFor(String username) {
        switch (username) {
            case "james_whitfield": return "James Whitfield";
            case "amara_klein": return "Amara Klein";
            case "dan_petrov": return "Dan Petrov";
            default: return username;
        }
    }
}