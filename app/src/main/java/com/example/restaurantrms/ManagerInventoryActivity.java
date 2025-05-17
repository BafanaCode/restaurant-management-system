package com.example.restaurantrms;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ManagerInventoryActivity extends AppCompatActivity {

    EditText itemNameInput, quantityInput, reorderThresholdInput;
    Button addItemBtn;
    LinearLayout inventoryListLayout;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_inventory);

        itemNameInput = findViewById(R.id.itemNameInput);
        quantityInput = findViewById(R.id.quantityInput);
        reorderThresholdInput = findViewById(R.id.reorderThresholdInput);
        addItemBtn = findViewById(R.id.addInventoryButton);
        inventoryListLayout = findViewById(R.id.inventoryListLayout);

        db = FirebaseFirestore.getInstance();

        loadInventory();

        addItemBtn.setOnClickListener(v -> {
            String name = itemNameInput.getText().toString().trim();
            String qtyStr = quantityInput.getText().toString().trim();
            String thresholdStr = reorderThresholdInput.getText().toString().trim();

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(qtyStr) || TextUtils.isEmpty(thresholdStr)) {
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            Map<String, Object> item = new HashMap<>();
            item.put("item_name", name);
            item.put("quantity_in_stock", Integer.parseInt(qtyStr));
            item.put("reorder_threshold", Integer.parseInt(thresholdStr));
            item.put("last_updated", System.currentTimeMillis());

            db.collection("inventory").add(item).addOnSuccessListener(doc -> {
                Toast.makeText(this, "Item Added", Toast.LENGTH_SHORT).show();
                itemNameInput.setText("");
                quantityInput.setText("");
                reorderThresholdInput.setText("");
                loadInventory();
            });
        });
    }

    private void loadInventory() {
        inventoryListLayout.removeAllViews();

        db.collection("inventory").get().addOnSuccessListener(query -> {
            for (var doc : query) {
                String name = doc.getString("item_name");
                Long qty = doc.getLong("quantity_in_stock");
                Long threshold = doc.getLong("reorder_threshold");

                TextView view = new TextView(this);
                String status = (qty != null && threshold != null && qty <= threshold) ? "⚠️ LOW STOCK!" : "";
                view.setText(name + " | Qty: " + qty + " | Threshold: " + threshold + " " + status);
                view.setPadding(10, 10, 10, 10);
                inventoryListLayout.addView(view);
            }
        });
    }
}
