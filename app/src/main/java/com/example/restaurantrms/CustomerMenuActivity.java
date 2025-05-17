package com.example.restaurantrms;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.*;
import com.example.restaurantrms.models.MenuItem; // ✅ <--- Add this
import com.google.firebase.firestore.*;

import java.util.*;

public class CustomerMenuActivity extends AppCompatActivity {

    FirebaseFirestore db;
    RecyclerView recyclerView;
    List<MenuItem> menuItems = new ArrayList<>();
    MenuAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_menu);

        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerCustomerMenu);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MenuAdapter(menuItems, false); // editable false
        recyclerView.setAdapter(adapter);

        db.collection("menu_items").get().addOnSuccessListener(snap -> {
            menuItems.clear();
            for (DocumentSnapshot doc : snap) {
                MenuItem item = doc.toObject(MenuItem.class);
                item = new MenuItem(doc.getId(), item.getName(), item.getDescription(), item.getPrice(), item.isAvailable());
                if (item.isAvailable()) menuItems.add(item);
            }
            adapter.notifyDataSetChanged();
        });
    }
}
