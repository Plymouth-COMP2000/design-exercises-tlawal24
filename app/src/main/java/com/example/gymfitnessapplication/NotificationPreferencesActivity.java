package com.example.gymfitnessapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class NotificationPreferencesActivity extends AppCompatActivity {

    private Switch bookingNotifications;
    private Switch cancellationNotifications;
    private NotificationDao notificationDao;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_preferences);

        bookingNotifications = findViewById(R.id.bookingNotifications);
        cancellationNotifications = findViewById(R.id.cancellationNotifications);
        Button saveBtn = findViewById(R.id.savePreferencesBtn);

        notificationDao = new NotificationDao(this);

        username = getSharedPreferences("gym_prefs", MODE_PRIVATE)
                .getString("logged_in_user", "unknown_member");

        // Load existing preferences
        boolean[] prefs = notificationDao.getPreferences(username);
        boolean bookingEnabled = prefs[0];
        boolean cancellationEnabled = prefs[1];

        // Set initial checkbox states
        bookingNotifications.setChecked(bookingEnabled);
        cancellationNotifications.setChecked(cancellationEnabled);

        saveBtn.setOnClickListener(v -> {
            notificationDao.savePreferences(
                    username,
                    bookingNotifications.isChecked(),
                    cancellationNotifications.isChecked()
            );

            Toast.makeText(this, "Preferences saved", Toast.LENGTH_SHORT).show();
        });
    }
}
