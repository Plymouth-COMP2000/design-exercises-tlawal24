package com.example.gymfitnessapplication;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AccountDetailsActivity extends AppCompatActivity {

    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account_details);

        SharedPreferences prefs = getSharedPreferences("gym_prefs", MODE_PRIVATE);
        username = prefs.getString("logged_in_user", "unknown");

        TextView usernameValue = findViewById(R.id.usernameValue);
        EditText nameInput = findViewById(R.id.nameInput);
        usernameValue.setText(username);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("users", new String[]{"full_name"},
                "username = ?", new String[]{username}, null, null, null);
        if (cursor.moveToFirst()) {
            nameInput.setText(cursor.getString(cursor.getColumnIndexOrThrow("full_name")));
        }
        cursor.close();
        db.close();

        Button saveBtn = findViewById(R.id.saveAccountBtn);
        saveBtn.setOnClickListener(v -> {
            String newName = nameInput.getText().toString();

            SQLiteDatabase writeDb = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("full_name", newName);
            writeDb.update("users", values, "username = ?", new String[]{username});
            writeDb.close();

            Toast.makeText(this, "Account updated", Toast.LENGTH_SHORT).show();
            finish();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
