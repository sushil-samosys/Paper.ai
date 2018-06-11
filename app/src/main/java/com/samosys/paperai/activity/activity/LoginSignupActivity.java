package com.samosys.paperai.activity.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.parse.ParseUser;
import com.samosys.paperai.R;
import com.samosys.paperai.activity.utils.AppConstants;

public class LoginSignupActivity extends AppCompatActivity {

    private TextView txtSignin, txtSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signup);
        AppConstants.getstatusbar(LoginSignupActivity.this);
        findviews();

        txtSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser currentUser = ParseUser.getCurrentUser();
                if (currentUser != null) {
                    Intent intent = new Intent(LoginSignupActivity.this, HomeFeedActivity.class);
                    startActivity(intent);
                    finish();                    } else {
                    Intent intent=new Intent(LoginSignupActivity.this,LoginActivity.class);
                    startActivity(intent);
                   // finish();
                }


            }
        });
        txtSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginSignupActivity.this,SignupActivity.class);
                startActivity(intent);

            }
        });

    }

    private void findviews() {
        txtSignin = (TextView) findViewById(R.id.txtSignin);
        txtSignup = (TextView) findViewById(R.id.txtSignup);
    }

}
