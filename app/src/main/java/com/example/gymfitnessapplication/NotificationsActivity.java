package com.example.gymfitnessapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class NotificationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        NotificationDao notificationDao = new NotificationDao(this);

        String username = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
                .getString("logged_in_user", "unknown_user");

        LinearLayout container = findViewById(R.id.notificationsContainer);

        List<String[]> notifications = notificationDao.getNotificationsForUser(username);

        if (notifications.isEmpty()) {
            TextView empty = new TextView(this);
            empty.setText("No notifications yet.");
            empty.setTextSize(16);
            container.addView(empty);
            return;
        }

        for (String[] n : notifications) {
            String type = n[0];
            String message = n[1];
            String timestamp = n[2];

            View card = getLayoutInflater().inflate(R.layout.notification_card, null);

            TextView typeView = card.findViewById(R.id.notificationType);
            TextView messageView = card.findViewById(R.id.notificationMessage);
            TextView timeView = card.findViewById(R.id.notificationTimestamp);

            typeView.setText(type.equals("booking") ? "Booking" : "Cancellation");
            messageView.setText(message);
            timeView.setText(timestamp);

            container.addView(card);
        }
    }
}
