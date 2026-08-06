package com.example.gymfitnessapplication;

import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.LinkedHashMap;
import java.util.Map;

public class ManageAvailabilityActivity extends AppCompatActivity {

    private Map<Button, Boolean> slotState = new LinkedHashMap<>();
    private Map<Button, String> slotTimes = new LinkedHashMap<>();
    private String trainerUsername;
    private String currentDate = "2026-08-06"; // matches wireframe's "Thursday, 6 August"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manage_availability);

        SharedPreferences prefs = getSharedPreferences("gym_prefs", MODE_PRIVATE);
        trainerUsername = prefs.getString("logged_in_user", "unknown_trainer");

        Button slot9am = findViewById(R.id.slot9am);
        Button slot10am = findViewById(R.id.slot10am);
        Button slot11am = findViewById(R.id.slot11am);
        Button slot1pm = findViewById(R.id.slot1pm);
        Button slot2pm = findViewById(R.id.slot2pm);
        Button slot6pm = findViewById(R.id.slot6pm);

        slotTimes.put(slot9am, "9:00AM");
        slotTimes.put(slot10am, "10:00AM");
        slotTimes.put(slot11am, "11:00AM");
        slotTimes.put(slot1pm, "1:00PM");
        slotTimes.put(slot2pm, "2:00PM");
        slotTimes.put(slot6pm, "6:00PM");

        slotState.put(slot9am, true);
        slotState.put(slot10am, false);
        slotState.put(slot11am, true);
        slotState.put(slot1pm, true);
        slotState.put(slot2pm, false);
        slotState.put(slot6pm, true);

        for (Button slot : slotTimes.keySet()) {
            slot.setOnClickListener(v -> toggleSlot(slot));
        }

        Button saveButton = findViewById(R.id.saveAvailabilityButton);
        saveButton.setOnClickListener(v -> saveAvailability());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void toggleSlot(Button slot) {
        boolean isAvailable = slotState.get(slot);
        boolean newState = !isAvailable;
        slotState.put(slot, newState);

        if (newState) {
            slot.setBackgroundTintList(ColorStateList.valueOf(0xFFFFFFFF));
            slot.setTextColor(getResources().getColor(android.R.color.black));
        } else {
            slot.setBackgroundTintList(ColorStateList.valueOf(0xFF464646));
            slot.setTextColor(getResources().getColor(android.R.color.white));
        }
    }

    private void saveAvailability() {
        AvailabilityDao dao = new AvailabilityDao(this);
        for (Map.Entry<Button, String> entry : slotTimes.entrySet()) {
            Button slot = entry.getKey();
            String time = entry.getValue();
            boolean isAvailable = slotState.get(slot);
            dao.addSlot(trainerUsername, currentDate, time, isAvailable);
        }
        Toast.makeText(this, "Availability saved", Toast.LENGTH_SHORT).show();
        finish();
    }
}
