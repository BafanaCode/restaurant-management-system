package com.example.restaurantrms;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class CustomerFeedbackActivity extends AppCompatActivity {

    EditText feedbackInput;
    RatingBar ratingBar;
    Button submitButton;
    FirebaseFirestore db;
    FirebaseAuth mAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_feedback);

        feedbackInput = findViewById(R.id.feedbackInput);
        ratingBar = findViewById(R.id.ratingBar);
        submitButton = findViewById(R.id.submitFeedbackButton);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        submitButton.setOnClickListener(v -> {
            String feedbackText = feedbackInput.getText().toString().trim();
            float rating = ratingBar.getRating();

            if (TextUtils.isEmpty(feedbackText)) {
                Toast.makeText(this, "Please enter your feedback.", Toast.LENGTH_SHORT).show();
                return;
            }

            String uid = mAuth.getCurrentUser().getUid();

            Map<String, Object> feedback = new HashMap<>();
            feedback.put("user_id", uid);
            feedback.put("feedback", feedbackText);
            feedback.put("rating", rating);
            feedback.put("timestamp", System.currentTimeMillis());

            db.collection("feedbacks").add(feedback)
                    .addOnSuccessListener(doc -> {
                        Toast.makeText(this, "Feedback submitted. Thank you!", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        });
    }
}
