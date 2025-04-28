package com.firstapp.bbb;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class UserProfileActivity extends AppCompatActivity {

    private TextView fullNameText, emailText, dobText, mobileText;
    private Button homeButton;

    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        fullNameText = findViewById(R.id.fullNameText);
        emailText = findViewById(R.id.emailText);
        dobText = findViewById(R.id.dobText);
        mobileText = findViewById(R.id.mobileText);
        homeButton = findViewById(R.id.homeButton);

        // Home button logic
        homeButton.setOnClickListener(v -> {
            startActivity(new Intent(UserProfileActivity.this, MainActivity.class));
            finish();
        });

        // Load user data
        if(currentUser != null) {
            loadUserProfile(currentUser.getUid());
        } else {
            Toast.makeText(this, "No user logged in", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadUserProfile(String uid) {
        db.collection("users").document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String firstName = documentSnapshot.getString("firstName");
                        String lastName = documentSnapshot.getString("lastName");
                        String email = documentSnapshot.getString("email");
                        String dob = documentSnapshot.getString("dob");
                        String mobile = documentSnapshot.getString("mobile");

                        fullNameText.setText("Name: " + firstName + " " + lastName);
                        emailText.setText("Email: " + email);
                        dobText.setText("DOB: " + dob);
                        mobileText.setText("Mobile: " + mobile);
                    } else {
                        Toast.makeText(this, "User profile not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to load profile", Toast.LENGTH_SHORT).show();
                });
    }

}