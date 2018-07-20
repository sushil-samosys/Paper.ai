package com.samosys.paperai.activity.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.samosys.paperai.R;
import com.samosys.paperai.activity.utils.AppConstants;
import com.samosys.paperai.activity.utils.CustomFonts;

public class SignupActivity extends AppCompatActivity {
    private LinearLayout llBottomView;
    CustomFonts customFonts;
    private EditText dt_emailwork, dt_password, dt_conf_password;
    private TextView txtNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        AppConstants.getStatusBarHeight(SignupActivity.this);
        AppConstants.getstatusbar(SignupActivity.this);
        findviews();



        llBottomView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validation();

            }
        });
    }

    private void validation() {
        String email = dt_emailwork.getText().toString();
        String pwd = dt_password.getText().toString();
        String con_pwd = dt_conf_password.getText().toString();

        if (email.equals("") || email == null) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
        } else  if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter valid email", Toast.LENGTH_SHORT).show();
        } else if (pwd.equals("") || pwd == null) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();

        } else if (con_pwd.equals("") || con_pwd == null) {
            Toast.makeText(this, "Please enter confirm password", Toast.LENGTH_SHORT).show();

        } else if (!con_pwd.matches(pwd)) {
            Toast.makeText(this, "Confirm password not match", Toast.LENGTH_SHORT).show();
        } else {
            Log.e("email_name",email+">>"+pwd);
            Intent i = new Intent(SignupActivity.this, Secondsignupactivity.class);
            i.putExtra("email", email);
            i.putExtra("pwd", pwd);
            startActivity(i);
        }
    }

    private void findviews() {
        customFonts = new CustomFonts(SignupActivity.this);
        dt_emailwork = (EditText) findViewById(R.id.dt_emailwork);
        dt_password = (EditText) findViewById(R.id.dt_password);
        dt_conf_password = (EditText) findViewById(R.id.dt_conf_password);
        txtNext = (TextView) findViewById(R.id.txtNext);
        llBottomView = (LinearLayout) findViewById(R.id.llBottomView);

        dt_emailwork.setTypeface(customFonts.HelveticaNeue);
        dt_password.setTypeface(customFonts.HelveticaNeue);
        dt_conf_password.setTypeface(customFonts.HelveticaNeue);
        txtNext.setTypeface(customFonts.HelveticaNeueBold);

    }
}
