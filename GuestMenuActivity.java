package com.example.androidapp.ui.guest;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapp.R;
import com.example.androidapp.data.model.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class GuestMenuActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MenuAdapter adapter;
    List<MenuItem> menuList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_menu);

        recyclerView = findViewById(R.id.recyclerViewMenu);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Temporary test data
        menuList = new ArrayList<>();
        menuList.add(new MenuItem("Jollof Rice", "Spicy West African rice dish", 8.99));
        menuList.add(new MenuItem("Grilled Chicken", "Served with salad", 10.50));
        menuList.add(new MenuItem("Plantain", "Fried ripe plantain", 4.00));

        adapter = new MenuAdapter(menuList);
        recyclerView.setAdapter(adapter);
    }
}

