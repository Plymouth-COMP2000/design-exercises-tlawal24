package com.example.gymfitnessapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Bookasession extends AppCompatActivity {

    private Button slot4pm, slot5pm, slot6pm;
    private String selectedTime = "6:00 PM";
    private String trainerUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bookasession);

        trainerUsername = getIntent().getStringExtra("trainer_username");
        if (trainerUsername == null) trainerUsername = "unknown_trainer";

        TextView trainerNameText = findViewById(R.id.trainerName);
        trainerNameText.setText(displayNameFor(trainerUsername));

        slot4pm = findViewById(R.id.slot4pm);
        slot5pm = findViewById(R.id.slot5pm);
        slot6pm = findViewById(R.id.slot6pmSelected);

        slot4pm.setOnClickListener(v -> selectSlot("4:00 PM"));
        slot5pm.setOnClickListener(v -> selectSlot("5:00 PM"));
        slot6pm.setOnClickListener(v -> selectSlot("6:00 PM"));

        NotificationDao notificationDao = new NotificationDao(this);

        Button confirmButton = findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("gym_prefs", MODE_PRIVATE);
            String memberUsername = prefs.getString("logged_in_user", "unknown_member");

            TextView dateValue = findViewById(R.id.dateValue);
            String date = dateValue.getText().toString();

            String bookingReference = "BK" + System.currentTimeMillis();

            SessionDao sessionDao = new SessionDao(this);
            sessionDao.insertSession(trainerUsername, memberUsername, date, selectedTime, "confirmed", bookingReference);

            String trainerDisplayName = displayNameFor(trainerUsername);

            notificationDao.addNotificationIfEnabled(memberUsername, "booking",
                    "Your booking with " + trainerDisplayName + " was confirmed.");

            notificationDao.addNotificationIfEnabled(trainerUsername, "booking",
                    "New booking from " + memberUsername + " on " + date + " at " + selectedTime + ".");

            Toast.makeText(this, "Booking confirmed", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, Confirmation.class);
            intent.putExtra("trainer_display_name", trainerDisplayName);
            intent.putExtra("date", date);
            intent.putExtra("time", selectedTime);
            intent.putExtra("booking_reference", bookingReference);
            startActivity(intent);
        });

        Button cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(v -> finish());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private String displayNameFor(String username) {
        switch (username) {
            case "james_whitfield": return "James Whitfield";
            case "amara_klein": return "Amara Klein";
            case "dan_petrov": return "Dan Petrov";
            default: return username;
        }
    }

    private void selectSlot(String time) {
        selectedTime = time;
        TextView timeValue = findViewById(R.id.timeValue);
        timeValue.setText(time);

        resetSlotStyle(slot4pm);
        resetSlotStyle(slot5pm);
        resetSlotStyle(slot6pm);

        Button selected = time.equals("4:00 PM") ? slot4pm : time.equals("5:00 PM") ? slot5pm : slot6pm;
        selected.setBackgroundTintList(ColorStateList.valueOf(0xFF464646));
        selected.setTextColor(getResources().getColor(android.R.color.white));
    }

    private void resetSlotStyle(Button slot) {
        slot.setBackgroundTintList(ColorStateList.valueOf(0xFFFFFFFF));
        slot.setTextColor(getResources().getColor(android.R.color.black));
    }
}
