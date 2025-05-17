package com.example.restaurantrms;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.restaurantrms.adapters.OrderAdapter;
import com.example.restaurantrms.models.Order;
import com.google.firebase.firestore.*;

import java.util.ArrayList;
import java.util.List;

public class ChefOrderActivity extends AppCompatActivity {

    ListView orderListView;
    FirebaseFirestore db;
    List<Order> orderList = new ArrayList<>();
    OrderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_order);

        orderListView = findViewById(R.id.orderListView);
        db = FirebaseFirestore.getInstance();

        adapter = new OrderAdapter(this, orderList);
        orderListView.setAdapter(adapter);

        listenForOrders();
    }

    private void listenForOrders() {
        db.collection("orders")
                .whereIn("status", List.of("received", "preparing"))
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Toast.makeText(this, "Error loading orders", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    orderList.clear();
                    for (DocumentSnapshot doc : value.getDocuments()) {
                        Order order = doc.toObject(Order.class);
                        order.orderId = doc.getId(); // store doc ID to update
                        orderList.add(order);
                    }
                    adapter.notifyDataSetChanged();
                });
    }
}
