package com.example.restaurantrms;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.restaurantrms.models.Order;
import com.example.restaurantrms.models.OrderItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class WaiterOrderActivity extends AppCompatActivity {

    EditText tableInput, itemName, itemQty, itemNote;
    Button submitOrder;
    FirebaseFirestore db;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiter_order);

        tableInput = findViewById(R.id.tableInput);
        itemName = findViewById(R.id.itemName);
        itemQty = findViewById(R.id.itemQty);
        itemNote = findViewById(R.id.itemNote);
        submitOrder = findViewById(R.id.submitOrder);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() == null) {
            Toast.makeText(this, "Not logged in!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        submitOrder.setOnClickListener(v -> {
            String table = tableInput.getText().toString().trim();
            String item = itemName.getText().toString().trim();
            String qtyStr = itemQty.getText().toString().trim();
            String note = itemNote.getText().toString().trim();

            if (TextUtils.isEmpty(table) || TextUtils.isEmpty(item) || TextUtils.isEmpty(qtyStr)) {
                Toast.makeText(this, "Fill in all required fields", Toast.LENGTH_SHORT).show();
                return;
            }

            int qty = Integer.parseInt(qtyStr);

            List<OrderItem> items = new ArrayList<>();
            items.add(new OrderItem(item, qty, note));

            Order order = new Order(
                    null,
                    auth.getCurrentUser().getUid(),
                    table,
                    System.currentTimeMillis(),
                    "received",
                    items
            );

            db.collection("orders").add(order)
                    .addOnSuccessListener(doc -> {
                        Toast.makeText(this, "✅ Order placed!", Toast.LENGTH_SHORT).show();
                        finish(); // back to dashboard
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "❌ Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        });
    }
}
