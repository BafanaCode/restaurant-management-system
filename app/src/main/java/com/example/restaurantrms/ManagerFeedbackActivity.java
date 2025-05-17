package com.example.restaurantrms;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.*;

import com.example.restaurantrms.adapters.FeedbackAdapter;
import com.google.firebase.firestore.*;

import java.util.*;

public class ManagerFeedbackActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FeedbackAdapter adapter;
    List<Map<String, Object>> feedbackList = new ArrayList<>();
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_feedback);

        recyclerView = findViewById(R.id.recyclerFeedbacks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        db = FirebaseFirestore.getInstance();

        adapter = new FeedbackAdapter(feedbackList);
        recyclerView.setAdapter(adapter);

        loadFeedbacks();
    }

    private void loadFeedbacks() {
        db.collection("feedbacks").orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    feedbackList.clear();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        feedbackList.add(doc.getData());
                    }
                    adapter.notifyDataSetChanged();
                });
    }
}
