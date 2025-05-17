package com.example.restaurantrms.adapters;

import android.app.Activity;
import android.view.*;
import android.widget.*;
import com.example.restaurantrms.R;
import com.example.restaurantrms.models.Order;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class OrderAdapter extends BaseAdapter {

    Activity context;
    List<Order> orders;

    public OrderAdapter(Activity context, List<Order> orders) {
        this.context = context;
        this.orders = orders;
    }

    @Override
    public int getCount() { return orders.size(); }

    @Override
    public Object getItem(int i) { return orders.get(i); }

    @Override
    public long getItemId(int i) { return i; }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = context.getLayoutInflater().inflate(R.layout.item_order, null);

        TextView orderText = view.findViewById(R.id.orderInfo);
        Button statusButton = view.findViewById(R.id.statusButton);

        Order order = orders.get(position);

        String info = "Table: " + order.tableNumber + "\nStatus: " + order.status + "\nItems: " + order.items.size();
        orderText.setText(info);

        statusButton.setText(
                order.status.equals("received") ? "Set to PREPARING" : "Set to READY");

        statusButton.setOnClickListener(v -> {
            String newStatus = order.status.equals("received") ? "preparing" : "ready";
            FirebaseFirestore.getInstance().collection("orders")
                    .document(order.orderId)
                    .update("status", newStatus)
                    .addOnSuccessListener(unused ->
                            Toast.makeText(context, "Status updated", Toast.LENGTH_SHORT).show());
        });

        return view;
    }
}
