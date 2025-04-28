package com.firstapp.bbb;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        boolean onboardingShown = getSharedPreferences("appPrefs", MODE_PRIVATE)
                .getBoolean("onboardingShown", false);

        if(!onboardingShown) {
            startActivity(new Intent(this, OnboardingActivity.class));
            finish();
            return;
        }

        Button logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(v -> logoutUser());

        Button createPledgeButton = findViewById(R.id.createPledgeButton);
        Button viewPledgesButton = findViewById(R.id.viewPledgesButton);
        Button myPledgesButton = findViewById(R.id.myPledgesButton);
        Button myProgressButton = findViewById(R.id.myProgressButton);
        Button notificationsButton = findViewById(R.id.notificationsButton);




      notificationsButton.setOnClickListener(v -> {
          Intent intent = new Intent(MainActivity.this, NotificationsActivity.class);
          startActivity(intent);
      });

      myPledgesButton.setOnClickListener(v -> {
         Intent intent = new Intent(MainActivity.this, MyPledgesActivity.class);
         startActivity(intent);

      });

      viewPledgesButton.setOnClickListener(v -> {
          startActivity(new Intent(MainActivity.this, PublicPledgeBoardActivity.class));
      });

      createPledgeButton.setOnClickListener(v -> {
          startActivity(new Intent(MainActivity.this, PledgeActivity.class));
      });

      myProgressButton.setOnClickListener(v -> {
          startActivity(new Intent(MainActivity.this, MyProgressActivity.class));
      });






    }


    private void logoutUser() {
     FirebaseAuth.getInstance().signOut();

        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();

        //  back to the login Activity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();


    }

}