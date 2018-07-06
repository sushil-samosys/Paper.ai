package com.samosys.paperai.activity.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
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
import com.auth0.android.authentication.AuthenticationAPIClient;
import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.callback.BaseCallback;
import com.auth0.android.management.ManagementException;
import com.auth0.android.management.UsersAPIClient;
import com.auth0.android.provider.AuthCallback;
import com.auth0.android.provider.WebAuthProvider;
import com.auth0.android.result.Credentials;
import com.auth0.android.result.UserProfile;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.samosys.paperai.R;
import com.samosys.paperai.activity.utils.AppConstants;
import com.samosys.paperai.activity.utils.CustomFonts;

import java.io.File;
import java.io.IOException;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;


public class LoginActivity extends AppCompatActivity {
    public static String KEY_ACCESS_TOKEN = "";
    TextView txtForgot;
    CustomFonts customFonts;
    RelativeLayout RL_logWithpapr, rl_slack;
    EditText edt_workEmail, edt_workpwd;
    Auth0 auth0;
    private UserProfile userProfile;
    private AuthenticationAPIClient authenticationAPIClient;
    private UsersAPIClient usersClient;
    private ImageView imgback;
    private ACProgressFlower dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AppConstants.getstatusbar(LoginActivity.this);
//        slackToken = AppConstants.loadPreferences(LoginActivity.this, "slack_token");

        findview();
        AppConstants.savePreferences(LoginActivity.this, "projectID", "00");
        AppConstants.savePreferences(LoginActivity.this, "workid", "00");


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

                loginwithslack();
                //logged();
//                doLogin();

//                slcak();
            }
        });
    }


    private void login() {


        String email = edt_workEmail.getText().toString();
        String pwd = edt_workpwd.getText().toString();
        if (email.equals("") || email == null) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
        } else if (!AppConstants.isEmailValid(email)) {
            Toast.makeText(this, "Please enter valid email", Toast.LENGTH_SHORT).show();
        } else if (pwd.equals("") || pwd == null) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
        } else {
            dialog = new ACProgressFlower.Builder(this)
                    .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                    .themeColor(Color.WHITE)

                    .fadeColor(Color.DKGRAY).build();
            dialog.show();
            callparseaAPI(email, pwd, "1", "", "");


        }
    }

    private void callparseaAPI(final String email, final String pwd, final String type, final String nickname, final String pictureURL) {
//        final ProgressDialog dialog = AppConstants.showProgressDialog(LoginActivity.this, "Please wait...");


        ParseUser.logInInBackground(email, pwd, new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                Log.e("callparseaAPI1", user + "");

                if (user != null) {
                    // Hooray! The user is logged in.

                    AppConstants.savePreferences(LoginActivity.this, "email", user.getEmail());
                    AppConstants.savePreferences(LoginActivity.this, "name", user.getUsername());
                    AppConstants.savePreferences(LoginActivity.this, "login", "1");
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }


                    Intent intent = new Intent(LoginActivity.this, HomeFeedActivity.class);

                    AppConstants.savePreferences(LoginActivity.this, "workid", "00");
                    startActivity(intent);
                } else {


                    Log.e("callparseaAPI22", e.getMessage());
//                    if (dialog.isShowing()) {
//                        dialog.dismiss();
//                    }
                    if (type.equals("1")) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {

                        SignupAPI(email, pwd, type, nickname, pictureURL);
                    }
                    // Signup failed. Look at the ParseException to see what happened.
                }
            }
        });


    }

    private void SignupAPI(String email, String pwd, String type, String nickname, String pictureURL) {
        ParseUser user = new ParseUser();
        user.setUsername(email);
        user.setPassword(pwd);
        user.setEmail(email);
        ParseFile parseFile = null;
// other fields can be set just like with ParseObject
        user.put("fullname", nickname);
        user.put("title", "");
        user.put("passion", "");
//        pictureURL = "https://i2.wp.com/cdn.auth0.com/avatars/aj.png?ssl=1";

        if (!pictureURL.equals("")) {
            File newFile = new File(pictureURL);
            Uri selectedImage = Uri.fromFile(newFile);


            try {
                //  Bitmap bitmap = MediaStore.Images.Media.getBitmap(LoginActivity.this.getContentResolver(), selectedImage);

                // ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // Compress image to lower quality scale 1 - 100
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);//
//                byte[] image = stream.toByteArray();
                // String imageString = "abcde......"; //quite a long string
//                String a=(new String(utf8bytes, "utf-8")).getBytes("utf-16");
                byte[] imageBytes = pictureURL.getBytes("UTF-16");

                File file = new File(pictureURL);
                // Create the ParseFile
                Log.e("profileimage", file + "");
//                https://i2.wp.com/cdn.auth0.com/avatars/aj.png?ssl=1
//                parseFile = new ParseFile("postfile.jpg", imageBytes);
//                user.put("profileimage", parseFile);
                callparse(user);

            } catch (IOException e) {
                e.printStackTrace();
            }

//            callparse(user);
            Log.e("PARAMS", user.isAuthenticated() + "");
        }

//        callparse(user);
    }

    private void callparse(final ParseUser user) {


        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    // Hooray! Let them use the app now.
                    Toast.makeText(LoginActivity.this, "succes", Toast.LENGTH_SHORT).show();
                    AppConstants.savePreferences(LoginActivity.this, "email", user.getEmail());
                    AppConstants.savePreferences(LoginActivity.this, "name", user.getUsername());
                    AppConstants.savePreferences(LoginActivity.this, "login", "1");

                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Intent intent = new Intent(LoginActivity.this, CreateNjoinworkActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Log.e("MESAGE", e.getMessage());
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });
    }


    private void loginwithslack() {
//         auth0 = new Auth0(this);
        auth0 = new Auth0("IbQhkWZRKepbgpxUdaYAbTH21Lu8jIMM", "papr.auth0.com");
        auth0.setLoggingEnabled(true);
        auth0.setOIDCConformant(true);
        authenticationAPIClient = new AuthenticationAPIClient(auth0);
        WebAuthProvider.init(auth0)


                .withScheme("https")
//                .withScope("identity.basic,identity.email,identity.avatar")
                .withScope("openid profile email offline_access read:current_user")
                //Papr.ai.Papr-ai://papr.auth0.com/ios/Papr.ai.Papr-ai/callback
                .withAudience("https://papr.auth0.com/userinfo")

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
                        Log.e("credentials", exception.getMessage());
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


                                KEY_ACCESS_TOKEN = credentials.getAccessToken();

                                // CredentialsManager.saveCredentials(LoginActivity.this, credentials);
//                                Intent intent = new Intent(LoginActivity.this, HomeFeedActivity.class);
//                                intent.putExtra("id", "");
//
//                                startActivity(intent);
//                                finish();r23: An error occurred when trying to authenticate with the server.

                            }
                        });
                        Log.e("KEY_ACCESS_TOKEN", KEY_ACCESS_TOKEN);
                        //credentialsManager.saveCredentials(credentials);
                        getProfile(credentials.getAccessToken());
                    }
                });


    }

    private void getProfile(String credentials) {
        //auth0 = new Auth0(LoginActivity.this);
        auth0.setOIDCConformant(true);
        auth0.setLoggingEnabled(true);

        usersClient = new UsersAPIClient(auth0, credentials);
        authenticationAPIClient.userInfo(credentials)
                .start(new BaseCallback<UserProfile, AuthenticationException>() {
                    @Override
                    public void onSuccess(UserProfile userinfo) {

                        Log.e("userinfo", userinfo + "");
                        Log.e("credentials", userinfo.getName());
                        Log.e("credentials23", userinfo.getEmail());
                        callparseaAPI(userinfo.getEmail(), "123456", "2", userinfo.getNickname(), userinfo.getPictureURL());


                        usersClient.getProfile(userinfo.getId())
                                .start(new BaseCallback<UserProfile, ManagementException>() {
                                    @Override
                                    public void onSuccess(UserProfile profile) {
                                        userProfile = profile;
                                        Log.e("credentials_in", profile.getEmail());
                                        Log.e("credentials_in", profile.getId());
                                        runOnUiThread(new Runnable() {
                                            public void run() {

                                            }
                                        });
                                        callparseaAPI(profile.getEmail(), "123456", "2", profile.getNickname(), profile.getPictureURL());

//                                        refreshScreenInformation();
                                    }

                                    @Override
                                    public void onFailure(final ManagementException error) {

                                        //  Log.e("error", error.getMessage());
                                        runOnUiThread(new Runnable() {
                                            public void run() {
                                                //  Toast.makeText(LoginActivity.this, error.getMessage()+"", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });
                    }

                    @Override
                    public void onFailure(final AuthenticationException error) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                // Toast.makeText(LoginActivity.this, error.getMessage()+"", Toast.LENGTH_SHORT).show();
                            }
                        });

                        //Log.e("error23", error.getMessage());
                    }
                });
    }

    private void refreshScreenInformation() {


        Log.e("username", userProfile.getName());
        Log.e("useremail", userProfile.getEmail());
        Log.e("userImage", userProfile.getPictureURL());
//        if (userProfile.getPictureURL() != null) {
//            Picasso.with(this)
//                    .load(userProfile.getPictureURL())
//                    .into(userPicture);
//        }

        String country = (String) userProfile.getUserMetadata().get("country");
        Log.e("country", country);
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
