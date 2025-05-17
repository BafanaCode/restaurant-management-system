package com.example.restaurantrms;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.*;

import java.util.*;

public class ManagerAssignRolesActivity extends AppCompatActivity {

    LinearLayout userListLayout;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_roles);

        userListLayout = findViewById(R.id.userListLayout);
        db = FirebaseFirestore.getInstance();

        db.collection("users").get().addOnSuccessListener(docs -> {
            userListLayout.removeAllViews();

            for (DocumentSnapshot doc : docs) {
                String name = doc.getString("fullName");
                String role = doc.getString("role");
                String userId = doc.getId();

                LinearLayout row = new LinearLayout(this);
                row.setOrientation(LinearLayout.HORIZONTAL);

                TextView info = new TextView(this);
                info.setText(name + " (" + role + ")");
                info.setPadding(10, 10, 10, 10);

                Spinner roleSpinner = new Spinner(this);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_dropdown_item,
                        new String[]{"customer", "waiter", "chef", "manager"});
                roleSpinner.setAdapter(adapter);
                roleSpinner.setSelection(adapter.getPosition(role));

                Button updateBtn = new Button(this);
                updateBtn.setText("Update");

                updateBtn.setOnClickListener(v -> {
                    String selected = roleSpinner.getSelectedItem().toString();
                    db.collection("users").document(userId).update("role", selected)
                            .addOnSuccessListener(a -> Toast.makeText(this, "Role updated", Toast.LENGTH_SHORT).show());
                });

                row.addView(info);
                row.addView(roleSpinner);
                row.addView(updateBtn);

                userListLayout.addView(row);
            }
        });
    }
}
