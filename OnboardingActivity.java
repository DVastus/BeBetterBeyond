package com.firstapp.bbb;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import com.firstapp.bbb.databinding.ActivityOnboardingBinding;

public class OnboardingActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        // Load animati
        Animation fadeInSlideUp = AnimationUtils.loadAnimation(this, R.anim.fade_in_slide_up);
        Animation fadeInSlideUpButton = AnimationUtils.loadAnimation(this, R.anim.fade_in_slide_up);

        fadeInSlideUpButton.setStartOffset(300); // delay

        TextView welcomeText = findViewById(R.id.welcomeText);
        Button getStartedButton = findViewById(R.id.getStartedButton);

        welcomeText.startAnimation(fadeInSlideUp);
        getStartedButton.startAnimation(fadeInSlideUp); // button animates

      // getSupportActionBar().hide();


        getStartedButton.setOnClickListener(new View.OnClickListener() {
            @Override
                    public void onClick(View view) {
            getSharedPreferences("appPrefs", MODE_PRIVATE)
                    .edit()
                    .putBoolean("onboardingShown", true)
                    .apply();

            startActivity(new Intent(OnboardingActivity.this, MainActivity.class));
            finish();
            }
        });



    }
    }


