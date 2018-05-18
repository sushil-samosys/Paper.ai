package com.samosys.paperai.activity.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.auth0.android.Auth0;
import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.provider.AuthCallback;
import com.auth0.android.provider.WebAuthProvider;
import com.auth0.android.result.Credentials;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.samosys.paperai.R;
import com.samosys.paperai.activity.utils.AppConstants;
import com.samosys.paperai.activity.utils.CustomFonts;

public class LoginActivity extends AppCompatActivity {
    TextView txtForgot;
    CustomFonts customFonts;
    RelativeLayout RL_logWithpapr,rl_slack;
    EditText edt_workEmail, edt_workpwd;
    private ImageView imgback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AppConstants.getstatusbar(LoginActivity.this);

        findview();
        txtForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, Forgotpassword.class);
                startActivity(intent);
            }
        });

        RL_logWithpapr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();

            }
        });

        rl_slack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "Working", Toast.LENGTH_SHORT).show();
//                loginwithslack();
                //logged();
            }
        });
    }


    private void login() {

        String email = edt_workEmail.getText().toString();
        String pwd = edt_workpwd.getText().toString();
        if (email.equals("")||email==null) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
        } else if (!AppConstants.isEmailValid(email)) {
            Toast.makeText(this, "Please enter valid email", Toast.LENGTH_SHORT).show();
        } else if (pwd.equals("")||pwd==null) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
        } else {
            final ProgressDialog dialog=AppConstants.showProgressDialog(LoginActivity.this,"Please wait...");
            ParseUser.logInInBackground(email, pwd, new LogInCallback() {
                public void done(ParseUser user, ParseException e) {
                    if (user != null) {
                        // Hooray! The user is logged in.

                        AppConstants.savePreferences(LoginActivity.this,"email",user.getEmail());
                        AppConstants.savePreferences(LoginActivity.this,"name",user.getUsername());
                        AppConstants.savePreferences(LoginActivity.this,"login","1");
                        if (dialog.isShowing()){
                            dialog.dismiss();
                        }
                        Intent intent = new Intent(LoginActivity.this, HomeFeedActivity.class);
                        intent.putExtra("id","");
                        AppConstants.savePreferences(LoginActivity.this, "workid", "00");
                        startActivity(intent);
                    } else {
                        Log.e("ERROR", e.getMessage());
                        if (dialog.isShowing()){
                            dialog.dismiss();
                        }
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        // Signup failed. Look at the ParseException to see what happened.
                    }
                }
            });

        }
    }

    private void loginwithslack() {
//        Auth0 auth0 = new Auth0(this);
        Auth0 auth0 = new Auth0("IbQhkWZRKepbgpxUdaYAbTH21Lu8jIMM", "papr.auth0.com");
        auth0.setOIDCConformant(true);
        WebAuthProvider.init(auth0)
                .withScheme("demo")
                .withScope("read")
                //Papr.ai.Papr-ai://papr.auth0.com/ios/Papr.ai.Papr-ai/callback
                .withAudience(String.format("https://%s/userinfo", getString(R.string.com_auth0_domain)))

                .start(LoginActivity.this, new AuthCallback() {
                    @Override
                    public void onFailure(@NonNull final Dialog dialog) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.show();
                            }
                        });
                    }

                    @Override
                    public void onFailure(final AuthenticationException exception) {
                        Log.e("credentials",exception.getMessage());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this, "Error: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onSuccess(@NonNull final Credentials credentials) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Log.e("credentials",credentials.getAccessToken());

                                // CredentialsManager.saveCredentials(LoginActivity.this, credentials);
                                Intent intent = new Intent(LoginActivity.this, HomeFeedActivity.class);
                                intent.putExtra("id","");
                                startActivity(intent);
                                finish();

                            }
                        });

                    }
                });


    }

    private void findview() {

        customFonts = new CustomFonts(LoginActivity.this);
        RL_logWithpapr = (RelativeLayout) findViewById(R.id.RL_logWithpapr);
        rl_slack = (RelativeLayout) findViewById(R.id.rl_slack);
        txtForgot = (TextView) findViewById(R.id.txtForgot);
        edt_workEmail = (EditText) findViewById(R.id.edt_workEmail);
        edt_workpwd = (EditText) findViewById(R.id.edt_workpwd);
        txtForgot.setTypeface(customFonts.HelveticaNeue);
    }
}
