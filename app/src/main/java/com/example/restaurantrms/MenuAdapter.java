package com.example.restaurantrms;

import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.restaurantrms.models.MenuItem; // ✅ <--- Add this
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

    private final List<MenuItem> items;
    private final boolean editable;

    public MenuAdapter(List<MenuItem> items, boolean editable) {
        this.items = items;
        this.editable = editable;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override public int getItemCount() { return items.size(); }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int pos) {
        MenuItem item = items.get(pos);
        holder.name.setText(item.getName());
        holder.desc.setText(item.getDescription());
        holder.price.setText("R" + item.getPrice());
        holder.status.setText(item.isAvailable() ? "Available" : "Not Available");

        if (editable) {
            holder.delete.setVisibility(View.VISIBLE);
            holder.delete.setOnClickListener(v -> {
                FirebaseFirestore.getInstance().collection("menu_items").document(item.getItemId())

                        .delete().addOnSuccessListener(unused -> {
                            items.remove(pos);
                            notifyDataSetChanged();
                        });
            });
        } else {
            holder.delete.setVisibility(View.GONE);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, desc, price, status;
        ImageButton delete;
        ViewHolder(View item) {
            super(item);
            name = item.findViewById(R.id.menuItemName);
            desc = item.findViewById(R.id.menuItemDesc);
            price = item.findViewById(R.id.menuItemPrice);
            status = item.findViewById(R.id.menuItemStatus);
            delete = item.findViewById(R.id.deleteButton);
        }
    }
}
