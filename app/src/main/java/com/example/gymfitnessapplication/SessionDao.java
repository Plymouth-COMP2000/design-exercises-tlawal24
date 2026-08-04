package com.example.gymfitnessapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class SessionDao {

    private DatabaseHelper dbHelper;

    public SessionDao(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void insertSession(String trainerUsername, String memberUsername,
                              String date, String time, String status, String bookingReference) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("trainer_username", trainerUsername);
        values.put("member_username", memberUsername);
        values.put("date", date);
        values.put("time", time);
        values.put("status", status);
        values.put("booking_reference", bookingReference);
        db.insert("sessions", null, values);
        db.close();
    }
}
