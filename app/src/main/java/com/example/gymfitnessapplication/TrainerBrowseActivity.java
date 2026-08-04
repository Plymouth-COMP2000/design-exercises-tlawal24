package com.example.gymfitnessapplication;

import android.os.Bundle;
import android.content.Intent;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class TrainerBrowseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_browse);
        LinearLayout trainerCard1 = findViewById(R.id.trainerCard1);
        trainerCard1.setOnClickListener(v -> {
            Intent intent = new Intent(TrainerBrowseActivity.this, Bookasession.class);
            startActivity(intent);
        });


        AvailabilityDao dao = new AvailabilityDao(this);

        // TEMPORARY: seed test slots so there's data to display
        // (Remove this once real trainers add their own availability via Manage Availability)
        seedTestDataIfEmpty(dao);

        updateNextAvailable(dao, "james_whitfield", R.id.trainer1NextSlot);
        updateNextAvailable(dao, "amara_klein", R.id.trainer2NextSlot);
        updateNextAvailable(dao, "dan_petrov", R.id.trainer3NextSlot);
    }

    private void seedTestDataIfEmpty(AvailabilityDao dao) {
        if (dao.getSlotsForTrainer("james_whitfield").isEmpty()) {
            dao.addSlot("james_whitfield", "2026-08-06", "6:00 PM", true);
        }
        if (dao.getSlotsForTrainer("amara_klein").isEmpty()) {
            dao.addSlot("amara_klein", "2026-08-07", "9:00 AM", true);
        }
        if (dao.getSlotsForTrainer("dan_petrov").isEmpty()) {
            dao.addSlot("dan_petrov", "2026-08-10", "4:00 PM", true);
        }
    }

    private void updateNextAvailable(AvailabilityDao dao, String trainerUsername, int textViewId) {
        List<String[]> slots = dao.getSlotsForTrainer(trainerUsername);
        TextView slotText = findViewById(textViewId);

        String nextAvailable = "No slots available";
        for (String[] slot : slots) {
            String date = slot[1];
            String time = slot[2];
            String isAvailable = slot[3];
            if (isAvailable.equals("1")) {
                nextAvailable = "Next available: " + date + ", " + time;
                break;
            }
        }
        slotText.setText(nextAvailable);
    }
}