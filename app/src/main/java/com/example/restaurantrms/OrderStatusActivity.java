package com.example.restaurantrms;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;

public class OrderStatusActivity extends AppCompatActivity {

    LinearLayout orderStatusLayout;
    FirebaseFirestore db;
    FirebaseAuth auth;
    String userRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        orderStatusLayout = findViewById(R.id.orderStatusLayout);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        fetchUserRoleAndLoadOrders();
    }

    private void fetchUserRoleAndLoadOrders() {
        String uid = auth.getCurrentUser().getUid();
        db.collection("users").document(uid).get().addOnSuccessListener(doc -> {
            userRole = doc.getString("role").toLowerCase();
            loadOrders(uid);
        });
    }

    private void loadOrders(String uid) {
        Query query;

        // Waiter: load their own orders | Customer: by table | Chef: skip this view
        if (userRole.equals("waiter")) {
            query = db.collection("orders").whereEqualTo("waiterId", uid);
        } else if (userRole.equals("customer")) {
            query = db.collection("orders"); // Assume customers view all — could be filtered by table/customer later
        } else {
            return; // Not needed for chef
        }

        query.get().addOnSuccessListener(queryDocumentSnapshots -> {
            orderStatusLayout.removeAllViews();

            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                String docId = doc.getId();
                String table = doc.getString("table_number");
                String status = doc.getString("status");

                View itemView = getLayoutInflater().inflate(R.layout.item_order_status, null);
                TextView orderInfo = itemView.findViewById(R.id.orderStatusInfo);
                Button serveButton = itemView.findViewById(R.id.markAsServedButton);

                orderInfo.setText("Table: " + table + "\nStatus: " + status);

                // Only Waiter can mark "Ready" → "Served"
                if (userRole.equals("waiter") && status.equals("ready")) {
                    serveButton.setVisibility(View.VISIBLE);
                    serveButton.setOnClickListener(v -> {
                        db.collection("orders").document(docId)
                                .update("status", "served")
                                .addOnSuccessListener(unused -> {
                                    Toast.makeText(this, "Marked as Served", Toast.LENGTH_SHORT).show();
                                    loadOrders(uid);
                                });
                    });
                } else {
                    serveButton.setVisibility(View.GONE);
                }

                orderStatusLayout.addView(itemView);
            }
        });
    }
}
