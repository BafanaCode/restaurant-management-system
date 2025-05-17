package com.example.restaurantrms;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.*;

import java.util.*;

public class ManagerAssignTablesActivity extends AppCompatActivity {

    AutoCompleteTextView waiterSpinner;
    EditText tableNumberInput;
    Button assignButton;

    FirebaseFirestore db;
    List<String> waiterNames = new ArrayList<>();
    List<String> waiterUIDs = new ArrayList<>();
    ArrayAdapter<String> spinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_assign_tables);

        waiterSpinner = findViewById(R.id.waiterSpinner);
        tableNumberInput = findViewById(R.id.tableNumberInput);
        assignButton = findViewById(R.id.assignButton);

        db = FirebaseFirestore.getInstance();

        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, waiterNames);
        waiterSpinner.setAdapter(spinnerAdapter);

        loadWaiters();

        assignButton.setOnClickListener(v -> {
            int selectedIndex = waiterNames.indexOf(waiterSpinner.getText().toString());

            if (selectedIndex == -1) {
                Toast.makeText(this, "Please select a waiter", Toast.LENGTH_SHORT).show();
                return;
            }

            String waiterUID = waiterUIDs.get(selectedIndex);
            String table = tableNumberInput.getText().toString().trim();

            if (table.isEmpty()) {
                Toast.makeText(this, "Enter table number", Toast.LENGTH_SHORT).show();
                return;
            }

            Map<String, Object> data = new HashMap<>();
            data.put("assigned_waiter_id", waiterUID);
            data.put("table_number", table);

            db.collection("tables").add(data)
                    .addOnSuccessListener(doc -> Toast.makeText(this, "Table assigned!", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(this, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        });
    }

    private void loadWaiters() {
        db.collection("users")
                .whereEqualTo("role", "waiter")
                .get()
                .addOnSuccessListener(docs -> {
                    waiterNames.clear();
                    waiterUIDs.clear();

                    for (DocumentSnapshot doc : docs) {
                        String name = doc.getString("fullName");
                        String uid = doc.getId();

                        if (name != null && uid != null) {
                            waiterNames.add(name);
                            waiterUIDs.add(uid);
                        }
                    }
                    spinnerAdapter.notifyDataSetChanged();
                });
    }
}
