package com.firstapp.bbb;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Calendar;

public class PledgeActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private EditText pledgeDescriptionEditText;
    private EditText pledgeConditionEditText;
    private EditText mutualPledgeEditText;
    private Button savePledgeButton;
    private Button homeButton;
    private EditText pledgeDueDateEditText;
    private String selectedDueDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pledge);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        pledgeDescriptionEditText = findViewById(R.id.pledgeDescriptionEditText);
        pledgeConditionEditText = findViewById(R.id.pledgeConditionEditText);
        savePledgeButton = findViewById(R.id.savePledgeButton);
        homeButton = findViewById(R.id.homeButton);

        savePledgeButton.setOnClickListener(view -> savePledge());

        homeButton.setOnClickListener(view -> {
            startActivity(new Intent(PledgeActivity.this, MainActivity.class));
            finish();


        });
        pledgeDueDateEditText = findViewById(R.id.pledgeDueDateEditText);
        pledgeDueDateEditText.setOnClickListener(view -> showDatePicker());

        mutualPledgeEditText = findViewById(R.id.mutualPledgeEditText);
    }

    private void savePledge() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        String description = pledgeDescriptionEditText.getText().toString().trim();
        String condition = pledgeConditionEditText.getText().toString().trim();
        String mutualDescription = mutualPledgeEditText.getText().toString().trim();
        boolean isMutual = !mutualDescription.isEmpty();

        if (description.isEmpty() || condition.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }


        Map<String, Object> pledgeData = new HashMap<>();
        pledgeData.put("description", description);
        pledgeData.put("condition", condition);
        pledgeData.put("userId", user.getUid());
        pledgeData.put("timestamp", System.currentTimeMillis());
        pledgeData.put("isAccepted", false);
        pledgeData.put("isCompleted", false);
        //pledgeData.put("acceptedBy", null); // Should I add this?
        pledgeData.put("dueDate", selectedDueDate);

        pledgeData.put("mutualDescription", mutualDescription);
        pledgeData.put("isMutual", isMutual);


        db.collection("pledges")
                .add(pledgeData)
                .addOnSuccessListener(documentReference ->
                        Toast.makeText(this, "Pledge saved!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to save pledge", Toast.LENGTH_SHORT).show());

    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {
            selectedDueDate = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
            pledgeDueDateEditText.setText(selectedDueDate);
        }, year, month, day);

        datePickerDialog.show();

    }
}