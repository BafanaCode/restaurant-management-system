package com.example.restaurantrms;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;

public class WaiterAssignedTablesActivity extends AppCompatActivity {

    LinearLayout tableLayout;
    TextView noTablesMsg;
    FirebaseFirestore db;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiter_tables);

        tableLayout = findViewById(R.id.assignedTablesLayout);
        noTablesMsg = findViewById(R.id.noTablesMessage);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        String waiterId = mAuth.getCurrentUser().getUid();

        db.collection("tables")
                .whereEqualTo("assigned_waiter_id", waiterId)
                .get()
                .addOnSuccessListener(docs -> {
                    tableLayout.removeAllViews();

                    if (docs.isEmpty()) {
                        noTablesMsg.setVisibility(View.VISIBLE);
                    } else {
                        noTablesMsg.setVisibility(View.GONE);
                        for (DocumentSnapshot doc : docs) {
                            String number = doc.getString("table_number");
                            Long seats = doc.getLong("seats");

                            TextView tv = new TextView(this);
                            if (seats != null) {
                                tv.setText("Table: " + number + " | Seats: " + seats);
                            } else {
                                tv.setText("Table: " + number);
                            }
                            tv.setTextSize(18);
                            tv.setPadding(0, 16, 0, 16);
                            tableLayout.addView(tv);
                        }
                    }
                });
    }
}
