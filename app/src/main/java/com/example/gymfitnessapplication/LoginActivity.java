package com.example.gymfitnessapplication;

import android.os.Bundle;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        EditText emailInput = findViewById(R.id.emailInput);
        EditText passwordInput = findViewById(R.id.passwordInput);
        Button loginButton = findViewById(R.id.loginButton);
        TextView createAccountLink = findViewById(R.id.createAccountLink);

        passwordInput.setOnEditorActionListener((v, actionId, event) -> {
            loginButton.performClick();
            return true;
        });

        loginButton.setOnClickListener(v -> {
            String username = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty() ) {
                Toast.makeText(this, "Please enter both fields", Toast.LENGTH_SHORT).show();
                return;
            }

            DatabaseHelper dbhelper = new DatabaseHelper(this);
            SQLiteDatabase db = dbhelper.getReadableDatabase();

            Cursor cursor = db.rawQuery(
                    "SELECT role FROM users WHERE username = ? AND password = ?",
                    new String[]{username, password}
            );

            if (cursor.moveToFirst()) {
                String role = cursor.getString(0);
                cursor.close();

                SharedPreferences prefs = getSharedPreferences("gym_prefs", MODE_PRIVATE);
                prefs.edit().putString("logged_in_user", username).apply();

                Intent intent;
                if (role.equals("trainer")) {
                    intent = new Intent(LoginActivity.this, TrainerHomeActivity.class);
                } else {
                    intent = new Intent(LoginActivity.this, MemberHomeActivity.class);
                }
                startActivity(intent);
            } else {
                cursor.close();
                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
            }
        });

        createAccountLink.setOnClickListener(v -> {
            // RegisterActivity doesn't exist yet — placeholder for now
            Toast.makeText(this, "Create account screen not built yet", Toast.LENGTH_SHORT).show();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}