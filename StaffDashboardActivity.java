package com.example.androidapp.ui.staff;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.androidapp.R;

public class StaffDashboardActivity extends AppCompatActivity {

    Button btnViewBookings, btnManageMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_dashboard);

        btnViewBookings = findViewById(R.id.btnViewBookings);
        btnManageMenu = findViewById(R.id.btnManageMenu);

        btnViewBookings.setOnClickListener(v ->
                startActivity(new Intent(this, StaffBookingsActivity.class)));

        btnManageMenu.setOnClickListener(v ->
                startActivity(new Intent(this, StaffMenuActivity.class)));
    }
}
