package com.example.restaurantrms;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.*;

public class ManagerReservationsActivity extends AppCompatActivity {

    LinearLayout managerReservationLayout;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_reservations);

        managerReservationLayout = findViewById(R.id.managerReservationLayout);
        db = FirebaseFirestore.getInstance();

        loadAllReservations();
    }

    private void loadAllReservations() {
        db.collection("reservations")
                .orderBy("reservation_date")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    managerReservationLayout.removeAllViews();

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        String docId = doc.getId();
                        String date = doc.getString("reservation_date");
                        String time = doc.getString("reservation_time");
                        Long guests = doc.getLong("number_of_guests");
                        String status = doc.getString("status");

                        View resView = getLayoutInflater().inflate(R.layout.item_reservation_manager, null);

                        TextView info = resView.findViewById(R.id.reservationInfo);
                        Button confirmBtn = resView.findViewById(R.id.confirmReservationButton);
                        Button cancelBtn = resView.findViewById(R.id.cancelReservationButton);

                        info.setText("Date: " + date + "\nTime: " + time + "\nGuests: " + guests + "\nStatus: " + status);

                        // Hide buttons if already confirmed or cancelled
                        if (!status.equals("pending")) {
                            confirmBtn.setVisibility(View.GONE);
                            cancelBtn.setVisibility(View.GONE);
                        }

                        confirmBtn.setOnClickListener(v -> {
                            db.collection("reservations").document(docId)
                                    .update("status", "confirmed")
                                    .addOnSuccessListener(unused -> {
                                        Toast.makeText(this, "Reservation Confirmed", Toast.LENGTH_SHORT).show();
                                        loadAllReservations(); // Refresh list
                                    });
                        });

                        cancelBtn.setOnClickListener(v -> {
                            db.collection("reservations").document(docId)
                                    .update("status", "cancelled")
                                    .addOnSuccessListener(unused -> {
                                        Toast.makeText(this, "Reservation Cancelled", Toast.LENGTH_SHORT).show();
                                        loadAllReservations(); // Refresh list
                                    });
                        });

                        managerReservationLayout.addView(resView);
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to load reservations", Toast.LENGTH_SHORT).show());
    }
}
