package com.example.gymfitnessapplication;

import android.os.Bundle;

import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Confirmation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_confirmation);

        TextView trainerValue = findViewById(R.id.trainerValue);
        TextView dateValueConfirm = findViewById(R.id.dateValueConfirm);
        TextView timeValueConfirm = findViewById(R.id.timeValueConfirm);
        TextView referenceValue = findViewById(R.id.referenceValue);

        String trainerName = getIntent().getStringExtra("trainer_display_name");
        String date = getIntent().getStringExtra("date");
        String time = getIntent().getStringExtra("time");
        String reference = getIntent().getStringExtra("booking_reference");

        trainerValue.setText(trainerName != null ? trainerName : "-");
        dateValueConfirm.setText(date != null ? date : "-");
        timeValueConfirm.setText(time != null ? time : "-");
        referenceValue.setText(reference != null ? "#" + reference : "-");


        Button doneButton = findViewById(R.id.doneButton);
        doneButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MySessions.class);
            startActivity(intent);
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}