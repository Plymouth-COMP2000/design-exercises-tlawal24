package com.example.gymfitnessapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "gymfitness.db";
    private static final int DATABASE_VERSION = 1;
    private static final int DATABASE_VERSION = 2;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE sessions (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "trainer_username TEXT, " +
                "member_username TEXT, " +
                "date TEXT, " +
                "time TEXT, " +
                "status TEXT, " +
                "booking_reference TEXT)");

        db.execSQL("CREATE TABLE users (" +
                "username TEXT PRIMARY KEY, " +
                "password TEXT, " +
                "full_name TEXT, " +
                "role TEXT)");


        db.execSQL("CREATE TABLE availability (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "trainer_username TEXT, " +
                "date TEXT, " +
                "time TEXT, " +
                "is_available INTEGER)");

        db.execSQL("CREATE TABLE notifications (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT, " +
                "type TEXT, " +
                "message TEXT, " +
                "timestamp TEXT, " +
                "is_read INTEGER DEFAULT 0)");

        db.execSQL("CREATE TABLE notification_preferences (" +
                "username TEXT PRIMARY KEY, " +
                "bookings_enabled INTEGER DEFAULT 1, " +
                "cancellations_enabled INTEGER DEFAULT 1)");

        db.execSQL("INSERT INTO users (username, password, full_name, role) VALUES " +
                "('member1', 'password123', 'Matthew', 'member')");
        db.execSQL("INSERT INTO users (username, password, full_name, role) VALUES " +
                "('trainer1', 'password123', 'Sarah Coach', 'trainer')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS sessions");
        db.execSQL("DROP TABLE IF EXISTS availability");
        db.execSQL("DROP TABLE IF EXISTS notifications");
        db.execSQL("DROP TABLE IF EXISTS notification_preferences");
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }
}

