package com.example.restaurantrms;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.restaurantrms.models.Order;
import com.example.restaurantrms.models.OrderItem;
import com.example.restaurantrms.models.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;

import java.util.ArrayList;
import java.util.List;

public class PlaceOrderActivity extends AppCompatActivity {

    EditText tableInput;
    Button submitOrder;
    FirebaseFirestore db;
    FirebaseAuth auth;

    List<MenuItem> menuList = new ArrayList<>();
    List<OrderItem> orderItems = new ArrayList<>();

    // Simulated selection for now
    OrderItem demoItem = new OrderItem("item123", 2, "No onions");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);

        tableInput = findViewById(R.id.tableInput);
        submitOrder = findViewById(R.id.submitOrder);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Temporary hard-coded order item
        orderItems.add(demoItem);

        submitOrder.setOnClickListener(v -> {
            String table = tableInput.getText().toString();
            String waiterId = auth.getCurrentUser().getUid();

            Order order = new Order(null, waiterId, table, System.currentTimeMillis(), "received", orderItems);

            db.collection("orders").add(order)
                    .addOnSuccessListener(doc -> {
                        Toast.makeText(this, "Order placed!", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Failed to place order", Toast.LENGTH_SHORT).show());
        });
    }
}

