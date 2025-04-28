package com.firstapp.bbb;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class PledgeListActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private PledgeAdapter adapter;
    private List<Pledge> pledgeList;
    private Button homeButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pledge_list);

        // Handle window insets (status/nav bars)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.pledgeRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // important!

        pledgeList = new ArrayList<>();
        adapter = new PledgeAdapter(this, pledgeList);
        recyclerView.setAdapter(adapter);

        loadPledges();

        homeButton = findViewById(R.id.homeButton);

        homeButton.setOnClickListener(view -> {
            startActivity(new Intent(PledgeListActivity.this, MainActivity.class));
        });


    }

    private void loadPledges() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("pledges")
                .whereEqualTo("userId", user.getUid()) // Filter
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    pledgeList.clear();
                    for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                        Pledge p = doc.toObject(Pledge.class);
                        if (p != null) {
                            p.setId(doc.getId());
                            pledgeList.add(p);
                        }
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(PledgeListActivity.this,
                                "Error loading pledges: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show()
                );
    }

}