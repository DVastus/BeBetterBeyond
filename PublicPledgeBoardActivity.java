package com.firstapp.bbb;

import android.os.Bundle;
import android.widget.Toast;
import android.widget.Button;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class PublicPledgeBoardActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private PledgeAdapter adapter;
    private List<Pledge> pledgeList;
    private Button homeButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_public_pledge_board);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = FirebaseFirestore.getInstance();
        pledgeList = new ArrayList<>();

        recyclerView = findViewById(R.id.publicPledgeRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PledgeAdapter(this, pledgeList);
        recyclerView.setAdapter(adapter);

        homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(v -> {
            startActivity(new Intent(PublicPledgeBoardActivity.this, MainActivity.class));
        });

        loadUnacceptedPledges();

    }

    private void loadUnacceptedPledges() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        String currentUserId = currentUser.getUid();

        db.collection("pledges")
                .whereEqualTo("isAccepted", false)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    pledgeList.clear();
                    for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                        Pledge p = doc.toObject(Pledge.class);
                    //    if(p != null && !currentUserId.equals(doc.getString("userId"))){
                     // if(p != null){
                        if (p != null && p.getUserId() != null && !p.getUserId().equals(currentUserId)) {
                            p.setId(doc.getId());
                            pledgeList.add(p);
                        }
                    }
                    adapter.notifyDataSetChanged(); // is this correct?
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error loading pledges: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }



}