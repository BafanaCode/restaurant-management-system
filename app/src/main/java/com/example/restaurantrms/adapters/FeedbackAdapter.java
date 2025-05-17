package com.example.restaurantrms.adapters;

import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantrms.R;

import java.util.List;
import java.util.Map;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.ViewHolder> {

    private final List<Map<String, Object>> feedbackList;

    public FeedbackAdapter(List<Map<String, Object>> feedbackList) {
        this.feedbackList = feedbackList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_feedback, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Map<String, Object> feedback = feedbackList.get(position);
        holder.feedbackText.setText((String) feedback.get("feedback"));

        // 🛠 Fix crash: check if rating exists
        if (feedback.get("rating") != null) {
            holder.ratingBar.setRating(((Number) feedback.get("rating")).floatValue());
        } else {
            holder.ratingBar.setRating(0); // default if missing
        }
    }


    @Override
    public int getItemCount() {
        return feedbackList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView feedbackText;
        RatingBar ratingBar;

        ViewHolder(View view) {
            super(view);
            feedbackText = view.findViewById(R.id.feedbackText);
            ratingBar = view.findViewById(R.id.ratingDisplay);
        }
    }
}
