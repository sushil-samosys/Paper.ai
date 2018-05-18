package com.samosys.paperai.activity.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.parse.ParseUser;
import com.samosys.paperai.R;
import com.samosys.paperai.activity.utils.AppConstants;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        AppConstants.getstatusbar(SplashActivity.this);
        startTimer();
    }

    private void startTimer() {
        Thread timer = new Thread() {
            public void run() {
                try {
                    //Display for 3 seconds
                    sleep(3000);
                } catch (InterruptedException e) {
                    // TODO: handle exception
                    e.printStackTrace();
                } finally {
                    ParseUser currentUser = ParseUser.getCurrentUser();
                    if (currentUser != null) {
                        Intent intent = new Intent(SplashActivity.this, HomeFeedActivity.class);
                        startActivity(intent);
                        finish();                    } else {
                        Intent intent = new Intent(SplashActivity.this, LoginSignupActivity.class);
                        startActivity(intent);
                        finish();

                    }

                }
            }
        };
        timer.start();
    }
}
