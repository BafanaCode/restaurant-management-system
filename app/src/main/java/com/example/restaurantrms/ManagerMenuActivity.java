
package com.example.restaurantrms;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.*;
import com.example.restaurantrms.models.MenuItem; // ✅ <--- Add this
import com.google.firebase.firestore.*;

import java.util.*;

public class ManagerMenuActivity extends AppCompatActivity {

    FirebaseFirestore db;
    RecyclerView recyclerView;
    List<MenuItem> menuItems = new ArrayList<>();
    MenuAdapter adapter;
    Button addItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_menu);

        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerMenu);
        addItem = findViewById(R.id.addMenuItem);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MenuAdapter(menuItems, true); // editable true
        recyclerView.setAdapter(adapter);

        loadMenuItems();

        addItem.setOnClickListener(v -> showAddDialog());
    }

    private void loadMenuItems() {
        db.collection("menu_items").get().addOnSuccessListener(snap -> {
            menuItems.clear();
            for (DocumentSnapshot doc : snap) {
                MenuItem item = doc.toObject(MenuItem.class);
                item = new MenuItem(doc.getId(), item.getName(), item.getDescription(), item.getPrice(), item.isAvailable());
                menuItems.add(item);
            }
            adapter.notifyDataSetChanged();
        });
    }

    private void showAddDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Menu Item");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        EditText nameInput = new EditText(this);
        nameInput.setHint("Name");
        EditText descInput = new EditText(this);
        descInput.setHint("Description");
        EditText priceInput = new EditText(this);
        priceInput.setHint("Price");
        CheckBox availability = new CheckBox(this);
        availability.setText("Available?");
        layout.addView(nameInput);
        layout.addView(descInput);
        layout.addView(priceInput);
        layout.addView(availability);

        builder.setView(layout);
        builder.setPositiveButton("Save", (dialog, which) -> {
            String name = nameInput.getText().toString();
            String desc = descInput.getText().toString();
            double price = Double.parseDouble(priceInput.getText().toString());
            boolean avail = availability.isChecked();

            MenuItem newItem = new MenuItem(null, name, desc, price, avail);
            db.collection("menu_items").add(newItem).addOnSuccessListener(doc -> {
                Toast.makeText(this, "Item added", Toast.LENGTH_SHORT).show();
                loadMenuItems();
            });
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
}
