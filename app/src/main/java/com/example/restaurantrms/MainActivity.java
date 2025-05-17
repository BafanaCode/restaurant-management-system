package com.example.restaurantrms;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db;
    FirebaseAuth mAuth;

    TextView userGreeting;
    Button goToOrders, goToReservations, goToInventory, goToChefOrders, logoutButton;
    Button goToFeedback, viewFeedbacksManager, goToDashboard;
    Button viewCustomerReservations, viewOrderStatus, viewReservationsManager;
    Button viewAllReservationsWaiter, browseMenu, manageMenu;
    Button viewAssignedTables, assignRolesButton,assignTablesButton;
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        userGreeting = findViewById(R.id.userGreeting);
        goToOrders = findViewById(R.id.goToOrders);
        goToReservations = findViewById(R.id.goToReservations);
        goToInventory = findViewById(R.id.goToInventory);
        goToChefOrders = findViewById(R.id.goToChefOrders);
        logoutButton = findViewById(R.id.logoutButton);
        goToFeedback = findViewById(R.id.goToFeedback);
        viewFeedbacksManager = findViewById(R.id.viewFeedbacksManager);
        goToDashboard = findViewById(R.id.goToDashboard);

        viewCustomerReservations = findViewById(R.id.viewCustomerReservations);
        viewOrderStatus = findViewById(R.id.viewOrderStatus);
        viewReservationsManager = findViewById(R.id.viewReservationsManager);
        viewAllReservationsWaiter = findViewById(R.id.viewAllReservationsWaiter);
        browseMenu = findViewById(R.id.browseMenu);
        manageMenu = findViewById(R.id.manageMenu);
        viewAssignedTables = findViewById(R.id.viewAssignedTables);
        assignRolesButton = findViewById(R.id.assignRolesButton);
        assignTablesButton = findViewById(R.id.assignTablesButton);


        // Hide all buttons initially
        hideAllButtons();

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Check session
        if (mAuth.getCurrentUser() == null) {
            Toast.makeText(this, "Session expired. Please log in again.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
            return;
        }

        String uid = mAuth.getCurrentUser().getUid();

        db.collection("users").document(uid).get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        String name = doc.getString("fullName");
                        String role = doc.getString("role");

                        userGreeting.setText("Welcome, " + name + " 👋 ");

                        switch (role.toLowerCase()) {
                            case "waiter":
                                goToOrders.setVisibility(View.VISIBLE);
                                viewOrderStatus.setVisibility(View.VISIBLE);
                                viewAllReservationsWaiter.setVisibility(View.VISIBLE);
                                viewAssignedTables.setVisibility(View.VISIBLE);
                                break;

                            case "customer":
                                goToReservations.setVisibility(View.VISIBLE);
                                viewCustomerReservations.setVisibility(View.VISIBLE);
                                browseMenu.setVisibility(View.VISIBLE);
                                viewOrderStatus.setVisibility(View.VISIBLE);
                                goToFeedback.setVisibility(View.VISIBLE);


                                break;

                            case "chef":
                                goToChefOrders.setVisibility(View.VISIBLE);
                                break;

                            case "manager":
                                manageMenu.setVisibility(View.VISIBLE);
                                goToInventory.setVisibility(View.VISIBLE);
                                viewReservationsManager.setVisibility(View.VISIBLE);
                                viewFeedbacksManager.setVisibility(View.VISIBLE);
                                goToDashboard.setVisibility(View.VISIBLE);
                                assignRolesButton.setVisibility(View.VISIBLE);
                                assignTablesButton.setVisibility(View.VISIBLE);
                                break;
                        }
                    } else {
                        Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
                    }
                });

        // Button Actions
        goToOrders.setOnClickListener(v -> startActivity(new Intent(this, WaiterOrderActivity.class)));
        goToInventory.setOnClickListener(v -> startActivity(new Intent(this, ManagerInventoryActivity.class)));
        viewAssignedTables.setOnClickListener(v -> startActivity(new Intent(this, WaiterAssignedTablesActivity.class)));
        assignRolesButton.setOnClickListener(v -> startActivity(new Intent(this, ManagerAssignRolesActivity.class)));
        goToFeedback.setOnClickListener(v -> startActivity(new Intent(this, CustomerFeedbackActivity.class)));
        goToDashboard.setOnClickListener(v -> startActivity(new Intent(this, ManagerDashboardActivity.class)));
        goToReservations.setOnClickListener(v -> startActivity(new Intent(this, CustomerReservationActivity.class)));
        goToChefOrders.setOnClickListener(v -> startActivity(new Intent(this, ChefOrderActivity.class)));
        viewCustomerReservations.setOnClickListener(v -> startActivity(new Intent(this, CustomerViewReservationsActivity.class)));
        viewAllReservationsWaiter.setOnClickListener(v -> startActivity(new Intent(this, WaiterViewReservationsActivity.class)));
        viewReservationsManager.setOnClickListener(v -> startActivity(new Intent(this, ManagerReservationsActivity.class)));
        viewOrderStatus.setOnClickListener(v -> startActivity(new Intent(this, OrderStatusActivity.class)));
        viewFeedbacksManager.setOnClickListener(v -> startActivity(new Intent(this, ManagerFeedbackActivity.class)));
        browseMenu.setOnClickListener(v -> startActivity(new Intent(this, CustomerMenuActivity.class)));
        manageMenu.setOnClickListener(v -> startActivity(new Intent(this, ManagerMenuActivity.class)));
        assignTablesButton.setOnClickListener(v -> startActivity(new Intent(this, ManagerAssignTablesActivity.class)));

        logoutButton.setOnClickListener(v -> {
            mAuth.signOut();
            Toast.makeText(MainActivity.this, "Logged out", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void hideAllButtons() {
        goToOrders.setVisibility(View.GONE);
        goToReservations.setVisibility(View.GONE);
        goToInventory.setVisibility(View.GONE);
        goToChefOrders.setVisibility(View.GONE);
        viewCustomerReservations.setVisibility(View.GONE);
        viewOrderStatus.setVisibility(View.GONE);
        viewReservationsManager.setVisibility(View.GONE);
        viewAllReservationsWaiter.setVisibility(View.GONE);
        browseMenu.setVisibility(View.GONE);
        manageMenu.setVisibility(View.GONE);
        goToFeedback.setVisibility(View.GONE);
        viewFeedbacksManager.setVisibility(View.GONE);
        goToDashboard.setVisibility(View.GONE);
        viewAssignedTables.setVisibility(View.GONE);
        assignRolesButton.setVisibility(View.GONE);
    }
}
