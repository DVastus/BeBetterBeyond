package com.firstapp.bbb;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MyPledgesActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private PledgeAdapter adapter;
    private List<Pledge> pledgeList;
    private Button homeButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_pledges);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = FirebaseFirestore.getInstance();
        pledgeList = new ArrayList<>();
        adapter = new PledgeAdapter(this, pledgeList);

        recyclerView = findViewById(R.id.myPledgesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(v -> {
            startActivity(new Intent(MyPledgesActivity.this, MainActivity.class));
        });

        loadMyPledges();

    }

    private void loadMyPledges() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = user.getUid();
        pledgeList.clear();

        db.collection("pledges")
                .get()
                .addOnSuccessListener(queryDocumentSnapshot -> {
                    for (DocumentSnapshot doc : queryDocumentSnapshot.getDocuments()) {
                        Pledge p = doc.toObject(Pledge.class);
                        if (p != null && (userId.equals(p.getAcceptedBy()) || userId.equals(doc.getString("userId")))) {
                            p.setId(doc.getId());
                            pledgeList.add(p);
                        }
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(MyPledgesActivity.this, "Error loading pledges: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show()
                );

    }

}