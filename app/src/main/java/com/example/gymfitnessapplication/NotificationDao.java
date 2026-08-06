package com.example.gymfitnessapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NotificationDao {

    private DatabaseHelper dbHelper;

    public NotificationDao(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // Insert a notification ONLY if the user has that type enabled
    public void addNotificationIfEnabled(String username, String type, String message) {

        boolean[] prefs = getPreferences(username);
        boolean bookingEnabled = prefs[0];
        boolean cancellationEnabled = prefs[1];

        if (type.equals("booking") && !bookingEnabled) return;
        if (type.equals("cancellation") && !cancellationEnabled) return;

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("type", type);
        values.put("message", message);
        values.put("timestamp", new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date()));
        values.put("is_read", 0);
        db.insert("notifications", null, values);
        db.close();
    }

    // Get all notifications for a user
    public List<String[]> getNotificationsForUser(String username) {
        List<String[]> notifications = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query("notifications",
                new String[]{"type", "message", "timestamp", "is_read"},
                "username = ?",
                new String[]{username},
                null, null, "timestamp DESC");

        while (cursor.moveToNext()) {
            String type = cursor.getString(cursor.getColumnIndexOrThrow("type"));
            String message = cursor.getString(cursor.getColumnIndexOrThrow("message"));
            String timestamp = cursor.getString(cursor.getColumnIndexOrThrow("timestamp"));
            String isRead = cursor.getString(cursor.getColumnIndexOrThrow("is_read"));
            notifications.add(new String[]{type, message, timestamp, isRead});
        }

        cursor.close();
        db.close();
        return notifications;
    }

    // Get preferences (default = true/true)
    public boolean[] getPreferences(String username) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query("notification_preferences",
                new String[]{"bookings_enabled", "cancellations_enabled"},
                "username = ?",
                new String[]{username},
                null, null, null);

        boolean bookings = true;
        boolean cancellations = true;

        if (cursor.moveToFirst()) {
            bookings = cursor.getInt(cursor.getColumnIndexOrThrow("bookings_enabled")) == 1;
            cancellations = cursor.getInt(cursor.getColumnIndexOrThrow("cancellations_enabled")) == 1;
        }

        cursor.close();
        db.close();
        return new boolean[]{bookings, cancellations};
    }

    // Save preferences (insert or update)
    public void savePreferences(String username, boolean bookingsEnabled, boolean cancellationsEnabled) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("bookings_enabled", bookingsEnabled ? 1 : 0);
        values.put("cancellations_enabled", cancellationsEnabled ? 1 : 0);

        db.insertWithOnConflict("notification_preferences", null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }
}
