package com.example.gymfitnessapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

public class AvailabilityDao {

    private DatabaseHelper dbHelper;

    public AvailabilityDao(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // Add a new slot for a trainer
    public void addSlot(String trainerUsername, String date, String time, boolean isAvailable) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("trainer_username", trainerUsername);
        values.put("date", date);
        values.put("time", time);
        values.put("is_available", isAvailable ? 1 : 0);
        db.insert("availability", null, values);
        db.close();
    }

    // Get all slots for a specific trainer
    public List<String[]> getSlotsForTrainer(String trainerUsername) {
        List<String[]> slots = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("availability",
                new String[]{"id", "date", "time", "is_available"},
                "trainer_username = ?",
                new String[]{trainerUsername},
                null, null, "date, time");

        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndexOrThrow("id"));
            String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
            String time = cursor.getString(cursor.getColumnIndexOrThrow("time"));
            String isAvailable = cursor.getString(cursor.getColumnIndexOrThrow("is_available"));
            slots.add(new String[]{id, date, time, isAvailable});
        }
        cursor.close();
        db.close();
        return slots;
    }

    // Mark a slot as booked (unavailable)
    public void markSlotUnavailable(int slotId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("is_available", 0);
        db.update("availability", values, "id = ?", new String[]{String.valueOf(slotId)});
        db.close();
    }

    // Mark a slot as available again (e.g. cancellation)
    public void markSlotAvailable(int slotId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("is_available", 1);
        db.update("availability", values, "id = ?", new String[]{String.valueOf(slotId)});
        db.close();
    }
}