package com.example.restaurantrms;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.*;

public class CustomerReservationActivity extends AppCompatActivity {

    EditText guestsField;
    TextView dateField, timeField;
    Button pickDateButton, pickTimeButton, submitButton, cancelButton;
    FirebaseFirestore db;
    FirebaseAuth mAuth;

    Calendar selectedDateTime = Calendar.getInstance();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_reservation);

        guestsField = findViewById(R.id.guestsField);
        dateField = findViewById(R.id.dateField);
        timeField = findViewById(R.id.timeField);
        pickDateButton = findViewById(R.id.pickDateButton);
        pickTimeButton = findViewById(R.id.pickTimeButton);
        submitButton = findViewById(R.id.submitReservationButton);
        cancelButton = findViewById(R.id.cancelReservationButton);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        pickDateButton.setOnClickListener(v -> {
            Calendar now = Calendar.getInstance();
            DatePickerDialog dp = new DatePickerDialog(this, (view, year, month, day) -> {
                selectedDateTime.set(Calendar.YEAR, year);
                selectedDateTime.set(Calendar.MONTH, month);
                selectedDateTime.set(Calendar.DAY_OF_MONTH, day);
                dateField.setText(day + "/" + (month + 1) + "/" + year);
            }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
            dp.show();
        });

        pickTimeButton.setOnClickListener(v -> {
            Calendar now = Calendar.getInstance();
            TimePickerDialog tp = new TimePickerDialog(this, (view, hour, minute) -> {
                selectedDateTime.set(Calendar.HOUR_OF_DAY, hour);
                selectedDateTime.set(Calendar.MINUTE, minute);
                timeField.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
            }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), true);
            tp.show();
        });

        submitButton.setOnClickListener(v -> {
            String guestCount = guestsField.getText().toString();
            String date = dateField.getText().toString();
            String time = timeField.getText().toString();

            if (TextUtils.isEmpty(guestCount) || TextUtils.isEmpty(date) || TextUtils.isEmpty(time)) {
                Toast.makeText(this, "Fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            String uid = mAuth.getCurrentUser().getUid();
            Map<String, Object> reservation = new HashMap<>();
            reservation.put("user_id", uid);
            reservation.put("reservation_date", date);
            reservation.put("reservation_time", time);
            reservation.put("number_of_guests", Integer.parseInt(guestCount));
            reservation.put("status", "pending");

            db.collection("reservations").add(reservation)
                    .addOnSuccessListener(docRef -> {
                        Toast.makeText(this, "Reservation Submitted!", Toast.LENGTH_SHORT).show();
                        finish(); // Close screen
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                    );
        });

        cancelButton.setOnClickListener(v -> finish());
    }
}
