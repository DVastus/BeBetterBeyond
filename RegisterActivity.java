package com.firstapp.bbb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.AuthResult;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.SetOptions;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import androidx.annotation.NonNull;
import android.widget.EditText;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mAuth = FirebaseAuth.getInstance();

        // Configure buttons
        configureBackButton();
        configureRegisterButton();


    }

    private void configureRegisterButton() {
        Button registerButton = findViewById(R.id.registerButton);

        EditText firstNameEditText = findViewById(R.id.firstNameEditText);
        EditText lastNameEditText = findViewById(R.id.lastNameEditText);
        EditText emailEditText = findViewById(R.id.emailEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);
        EditText dobEditText = findViewById(R.id.dobEditText);
        EditText mobileEditText = findViewById(R.id.mobileEditText);

        registerButton.setOnClickListener(view -> {
            String firstName = firstNameEditText.getText().toString().trim();
            String lastName = lastNameEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String dob = dobEditText.getText().toString().trim();
            String mobile = mobileEditText.getText().toString().trim();



            if(firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || dob.isEmpty() || mobile.isEmpty()) {
                Toast.makeText(this, "All sections need to be completed", Toast.LENGTH_SHORT).show();
                return;
            }

            if(password.length() < 6){
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, task -> {
                if(task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    Toast.makeText(RegisterActivity.this, "Registered: " + user.getEmail(), Toast.LENGTH_SHORT).show();
                    saveUserToFirestore(user, firstName, lastName, dob, mobile, email);

                } else {
                    Toast.makeText(RegisterActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

            });
        });
    }

    private void saveUserToFirestore(FirebaseUser user, String firstName, String lastName, String dob, String mobile, String email) {
        String uid = user.getUid();
      //  String email = user.getEmail();

        User userObj = new User(firstName, lastName, dob, mobile, email, uid);

        // Add user data to Firestore
        db.collection("users")
                .document(uid)
                .set(userObj, SetOptions.merge())
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "User data saved to Firestore", Toast.LENGTH_SHORT).show();
                    } else{
                        Toast.makeText(RegisterActivity.this, "Error saving data to Firestore", Toast.LENGTH_SHORT).show();
                    }
                });
    }




    private void configureBackButton() {
        Button backButton = (Button) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
            finish();
            }
        });
    }


    public static class User{
        private String firstName;
        private String lastName;
        private String dob;
        private String mobile;
        private String email;
        private String uid;

        public User(String firstName, String lastName, String dob, String mobile, String email, String uid) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.dob = dob;
            this.mobile = mobile;
            this.email = email;
            this.uid = uid;
        }

        public String getFirstName() {return firstName; }
        public String getLastName() {return lastName; }
        public String getDob() { return dob; }
        public String getMobile() { return mobile; }
        public String getEmail() {
            return email;
        }

        public String getUid(){
            return uid;
        }
    }

}