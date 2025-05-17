package com.example.restaurantrms;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;

import java.util.*;

public class CustomerViewReservationsActivity extends AppCompatActivity {

    LinearLayout reservationsLayout;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    String userId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_view_reservations);

        reservationsLayout = findViewById(R.id.reservationsLayout);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();

        fetchReservations();
    }

    private void fetchReservations() {
        db.collection("reservations")
                .whereEqualTo("user_id", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    reservationsLayout.removeAllViews();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        String docId = doc.getId();
                        String date = doc.getString("reservation_date");
                        String time = doc.getString("reservation_time");
                        Long guests = doc.getLong("number_of_guests");
                        String status = doc.getString("status");

                        View item = getLayoutInflater().inflate(R.layout.item_reservation, null);

                        TextView resInfo = item.findViewById(R.id.reservationInfo);
                        Button cancelBtn = item.findViewById(R.id.cancelReservationButton);

                        resInfo.setText("Date: " + date + "\nTime: " + time + "\nGuests: " + guests + "\nStatus: " + status);

                        // Show cancel button only if reservation is still pending
                        if (!"pending".equalsIgnoreCase(status)) {
                            cancelBtn.setVisibility(View.GONE);
                        }

                        cancelBtn.setOnClickListener(v -> {
                            db.collection("reservations").document(docId)
                                    .update("status", "cancelled")
                                    .addOnSuccessListener(unused -> {
                                        Toast.makeText(this, "Reservation Cancelled", Toast.LENGTH_SHORT).show();
                                        fetchReservations(); // refresh list
                                    });
                        });

                        reservationsLayout.addView(item);
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error loading reservations", Toast.LENGTH_SHORT).show());
    }
}
