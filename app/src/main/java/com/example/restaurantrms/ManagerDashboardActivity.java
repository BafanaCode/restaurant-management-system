package com.example.restaurantrms;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.*;

import java.util.*;

public class ManagerDashboardActivity extends AppCompatActivity {

    FirebaseFirestore db;
    TextView totalSalesView, totalOrdersView, popularItemView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_dashboard);

        totalSalesView = findViewById(R.id.totalSalesText);
        totalOrdersView = findViewById(R.id.totalOrdersText);
        popularItemView = findViewById(R.id.popularItemText);

        db = FirebaseFirestore.getInstance();

        fetchDashboardData();
    }

    private void fetchDashboardData() {
        db.collection("order_items").get().addOnSuccessListener(docs -> {
            final double[] totalSales = {0};
            int totalOrders = 0;
            Map<String, Integer> itemCountMap = new HashMap<>();

            for (DocumentSnapshot doc : docs) {
                Long quantity = doc.getLong("quantity");
                String itemId = doc.getString("item_id");

                if (quantity != null && itemId != null) {
                    totalOrders += quantity;
                    itemCountMap.put(itemId, itemCountMap.getOrDefault(itemId, 0) + quantity.intValue());
                }
            }

            int finalTotalOrders = totalOrders;
            db.collection("menu_items").get().addOnSuccessListener(menuDocs -> {
                Map<String, Double> priceMap = new HashMap<>();
                Map<String, String> itemNameMap = new HashMap<>();

                for (DocumentSnapshot menuItem : menuDocs) {
                    String id = menuItem.getId();
                    Double price = menuItem.getDouble("price");
                    String name = menuItem.getString("name");

                    if (id != null && price != null && name != null) {
                        priceMap.put(id, price);
                        itemNameMap.put(id, name);
                    }
                }

                for (String id : itemCountMap.keySet()) {
                    int qty = itemCountMap.get(id);
                    double price = priceMap.getOrDefault(id, 0.0);
                    totalSales[0] += qty * price;
                }

                // Safely get most popular item
                String topItemName = "N/A";
                if (!itemCountMap.isEmpty()) {
                    String topItemId = Collections.max(itemCountMap.entrySet(), Map.Entry.comparingByValue()).getKey();
                    topItemName = itemNameMap.getOrDefault(topItemId, "Unknown Item");
                }

                totalSalesView.setText("R " + String.format("%.2f", totalSales[0]));
                totalOrdersView.setText(String.valueOf(finalTotalOrders));
                popularItemView.setText(topItemName);
            });
        }).addOnFailureListener(e -> {
            totalSalesView.setText("Error");
            totalOrdersView.setText("Error");
            popularItemView.setText("Error");
        });
    }
}
