package com.samosys.paperai.activity.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;
import com.samosys.paperai.R;
import com.samosys.paperai.activity.utils.AppConstants;
import com.samosys.paperai.activity.utils.CustomFonts;

public class Forgotpassword extends AppCompatActivity {
    private TextView txt_forget;
    private CustomFonts customFonts;
    private LinearLayout llBottomforgot;
    private EditText edtforgot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
        AppConstants.getstatusbar(Forgotpassword.this);
        findview();
        llBottomforgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (edtforgot.getText().toString().equals("")) {
                    Toast.makeText(Forgotpassword.this, "Please enter email", Toast.LENGTH_SHORT).show();
                } else if (!AppConstants.isEmailValid(edtforgot.getText().toString())) {
                    Toast.makeText(Forgotpassword.this, "Please enter valid email", Toast.LENGTH_SHORT).show();
                } else {
                    ParseUser.requestPasswordResetInBackground(edtforgot.getText().toString(), new RequestPasswordResetCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Toast.makeText(Forgotpassword.this, "Password send to your email", Toast.LENGTH_SHORT).show();
                                // An email was successfully sent with reset instructions.
                            } else {
                                Log.e("error", e.getMessage());
                                Toast.makeText(Forgotpassword.this, "Email not registered", Toast.LENGTH_SHORT).show();
                                // Something went wrong. Look at the ParseException to see what's up.
                            }

                        }
                    });
                }
            }
        });
    }

    private void findview() {
        customFonts = new CustomFonts(Forgotpassword.this);
        llBottomforgot = (LinearLayout) findViewById(R.id.llBottomforgot);
        txt_forget = (TextView) findViewById(R.id.txt_forget);
        edtforgot = (EditText) findViewById(R.id.edtforgot);
        txt_forget.setTypeface(customFonts.HelveticaNeue);
        edtforgot.setTypeface(customFonts.HelveticaNeue);
    }
}
