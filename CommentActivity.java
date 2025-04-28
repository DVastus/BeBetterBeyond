package com.firstapp.bbb;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.EditText;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import android.util.Log;

public class CommentActivity extends AppCompatActivity {

    private RecyclerView commentsRecyclerView;
    private EditText commentEditText;
    private Button postCommentButton;

    private List<Comment> commentList;
    private CommentAdapter adapter;

    private FirebaseFirestore db;
    private String pledgeId; // To be passed via intent

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_comment);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Link views
        commentsRecyclerView = findViewById(R.id.commentsRecyclerView);
        commentEditText = findViewById(R.id.commentEditText);
        postCommentButton = findViewById(R.id.postCommentButton);

        commentList = new ArrayList<>();
        adapter = new CommentAdapter(commentList);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentsRecyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        // Get pledge ID from intent
        pledgeId = getIntent().getStringExtra("pledgeId");
        Toast.makeText(this, "Pledge ID: " + pledgeId, Toast.LENGTH_SHORT).show();
        if(pledgeId == null) {
            Toast.makeText(this, "Missing pledge ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadComments();

        postCommentButton.setOnClickListener(v -> postComment());

        Button backToHomeButton = findViewById(R.id.BackToHomeButton);
        backToHomeButton.setOnClickListener(v -> {
            finish(); //
        });
    }

    private void loadComments() {

        db.collection("comments")
                .whereEqualTo("pledgeId", pledgeId)
                .orderBy("timestamp")
                .addSnapshotListener((querySnapshots, e) -> {
                    if(e != null || querySnapshots == null) {
                        Toast.makeText(this, "Error loading comments", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Log.d("CommentActivity", "Comments fetched: " + querySnapshots.size());

                    commentList.clear();
                    for(DocumentSnapshot doc : querySnapshots.getDocuments()) {
                        Comment comment = doc.toObject(Comment.class);
                        if(comment != null) commentList.add(comment);
                    }
                    adapter.notifyDataSetChanged();
                });
    }

    private void postComment() {
        String message = commentEditText.getText().toString().trim();
        if(message.isEmpty()) return;

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Comment comment = new Comment(pledgeId, userId, message, System.currentTimeMillis());

        db.collection("comments").add(comment)
                .addOnSuccessListener(docRef -> {
                    commentEditText.setText("");
                    Toast.makeText(this, "Comment posted", Toast.LENGTH_SHORT).show(); // auto-scroll to new comment

                    // add the comment to the list
                    commentList.add(comment);
                    adapter.notifyItemInserted(commentList.size() - 1);
                    commentsRecyclerView.scrollToPosition(commentList.size() - 1);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to post comment", Toast.LENGTH_SHORT).show();
                });

    }


}