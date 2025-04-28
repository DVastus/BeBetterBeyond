package com.firstapp.bbb;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MyProgressActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    private TextView createdCountText;
    private TextView acceptedCountText;
    private TextView completedCountText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_progress);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button homeButton = findViewById(R.id.homeButton);

        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(MyProgressActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        createdCountText = findViewById(R.id.createdPledgesCount);
        acceptedCountText = findViewById(R.id.acceptedPledgesCount);
        completedCountText = findViewById(R.id.completedPledgesCount);

        if(currentUser != null) {
            loadUserProgress();
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadUserProgress() {
        String userId = currentUser.getUid();

        db.collection("pledges").get().addOnSuccessListener(queryDocumentSnapshots -> {
            int createdCount = 0;
            int acceptedCount = 0;
            int completedCount = 0;

            for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                String docUserId = doc.getString("userId");
                String acceptedBy = doc.getString("acceptedBy");
                Boolean isCompleted = doc.getBoolean("isCompleted");

                if(docUserId != null && docUserId.equals(userId)) {
                    createdCount++;
                }

                if(acceptedBy != null && acceptedBy.equals(userId)) {
                    acceptedCount++;
                    if(isCompleted != null && isCompleted) {
                        completedCount++;
                    }
                }
            }

            createdCountText.setText("Pledges Created: " + createdCount);
            acceptedCountText.setText("Pledges Accepted: " + acceptedCount);
            completedCountText.setText("Pledges Completed: " + completedCount);

        } ).addOnFailureListener(e -> {
            Toast.makeText(MyProgressActivity.this, "Error loading progress", Toast.LENGTH_SHORT).show();
        });
    }


}