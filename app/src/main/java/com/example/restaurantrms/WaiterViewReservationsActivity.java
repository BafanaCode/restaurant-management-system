package com.example.restaurantrms;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.*;

public class WaiterViewReservationsActivity extends AppCompatActivity {

    LinearLayout waiterReservationLayout;
    FirebaseFirestore db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiter_view_reservations);

        waiterReservationLayout = findViewById(R.id.waiterReservationLayout);
        db = FirebaseFirestore.getInstance();

        loadAllReservations();
    }

    private void loadAllReservations() {
        db.collection("reservations")
                .orderBy("reservation_date")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    waiterReservationLayout.removeAllViews();

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        String date = doc.getString("reservation_date");
                        String time = doc.getString("reservation_time");
                        Long guests = doc.getLong("number_of_guests");
                        String status = doc.getString("status");

                        View resView = getLayoutInflater().inflate(R.layout.item_reservation_waiter, null);

                        TextView info = resView.findViewById(R.id.reservationInfo);
                        info.setText("Date: " + date + "\nTime: " + time + "\nGuests: " + guests + "\nStatus: " + status);

                        waiterReservationLayout.addView(resView);
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to load reservations", Toast.LENGTH_SHORT).show());
    }
}
