package com.example.gymfitnessapplication;

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

public class TrainerSessionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_trainer_sessions);

        SharedPreferences prefs = getSharedPreferences("gym_prefs", MODE_PRIVATE);
        String trainerUsername = prefs.getString("logged_in_user", "unknown_trainer");

        LinearLayout sessionsList = findViewById(R.id.sessionsList);
        loadSessions(trainerUsername, sessionsList);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void loadSessions(String trainerUsername, LinearLayout container) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query("sessions",
                new String[]{"member_username", "date", "time", "status", "booking_reference"},
                "trainer_username = ?",
                new String[]{trainerUsername},
                null, null, "date, time");

        if (cursor.getCount() == 0) {
            TextView emptyText = new TextView(this);
            emptyText.setText("No bookings yet");
            emptyText.setTextColor(0xFF666666);
            emptyText.setGravity(Gravity.CENTER);
            emptyText.setPadding(0, 40, 0, 0);
            container.addView(emptyText);
        } else {
            while (cursor.moveToNext()) {
                String member = cursor.getString(cursor.getColumnIndexOrThrow("member_username"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                String time = cursor.getString(cursor.getColumnIndexOrThrow("time"));
                String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
                String reference = cursor.getString(cursor.getColumnIndexOrThrow("booking_reference"));

                container.addView(buildSessionCard(displayNameFor(member), date, time, status, reference));
            }
        }

        cursor.close();
        db.close();
    }

    private String displayNameFor(String username) {
        switch (username) {
            case "member1": return "Matthew";
            default: return username;
        }
    }

    private LinearLayout buildSessionCard(String member, String date, String time, String status, String reference) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setBackgroundResource(android.R.drawable.dialog_holo_light_frame);
        card.setPadding(32, 32, 32, 32);

        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        cardParams.setMargins(0, 0, 0, 24);
        card.setLayoutParams(cardParams);

        TextView memberText = new TextView(this);
        memberText.setText(member);
        memberText.setTextSize(16);
        memberText.setTypeface(null, android.graphics.Typeface.BOLD);

        TextView dateTimeText = new TextView(this);
        dateTimeText.setText(date + ", " + time);
        dateTimeText.setTextSize(12);
        dateTimeText.setTextColor(0xFF3C5AC8);

        TextView statusText = new TextView(this);
        statusText.setText("Status: " + status);
        statusText.setTextSize(12);
        statusText.setTextColor(0xFF666666);

        card.addView(memberText);
        card.addView(dateTimeText);
        card.addView(statusText);

        if (status.equals("confirmed")) {
            android.widget.Button cancelBtn = new android.widget.Button(this);
            cancelBtn.setText("Cancel");
            cancelBtn.setTextSize(12);
            cancelBtn.setOnClickListener(v -> {
                SessionDao sessionDao = new SessionDao(this);
                sessionDao.cancelSession(reference);

                SharedPreferences prefs = getSharedPreferences("gym_prefs", MODE_PRIVATE);
                String trainerUsername = prefs.getString("logged_in_user", "unknown_trainer");

                NotificationDao notificationDao = new NotificationDao(this);
                notificationDao.addNotificationIfEnabled(trainerUsername, "cancellation",
                        "Session with " + member + " was cancelled.");

                recreate();
            });

            card.addView(cancelBtn);
        }

        return card;
    }
}