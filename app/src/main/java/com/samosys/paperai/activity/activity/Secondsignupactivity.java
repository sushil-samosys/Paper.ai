package com.samosys.paperai.activity.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.samosys.paperai.R;
import com.samosys.paperai.activity.utils.AppConstants;
import com.samosys.paperai.activity.utils.CustomFonts;
import com.samosys.paperai.activity.utils.Utility;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class Secondsignupactivity extends AppCompatActivity {
    CustomFonts customFonts;
    RelativeLayout rl_profilePic;
    ImageView user_image;
    String email = "", pwd = "";
    LinearLayout llSignup;
    EditText edt_fullname, edt_title, edt_passion;
    String image = "";
    ParseFile file = null;
    Bitmap bitmap = null;
    private TextView text, txtTerm1, txtTerm2;
    private String userChoosenTask;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private ACProgressFlower dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secondsignupactivity);
        AppConstants.getTranparentstatusbar(Secondsignupactivity.this);
        email = getIntent().getStringExtra("email");
        pwd = getIntent().getStringExtra("pwd");
//        Log.e("email_name",email+">>"+pwd);
        findviews();
        rl_profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });


        llSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                validation();
            }
        });

    }

    private void validation() {
        final String fullname = edt_fullname.getText().toString();
        final String title = edt_title.getText().toString();
        final String passion = edt_passion.getText().toString();

        if (fullname.equals("") || fullname == null) {
            Toast.makeText(Secondsignupactivity.this, "Please enter fullname", Toast.LENGTH_SHORT).show();
        } else if (title.equals("") || title == null) {
            Toast.makeText(Secondsignupactivity.this, "Please enter title", Toast.LENGTH_SHORT).show();

        } else if (passion.equals("") || passion == null) {
            Toast.makeText(Secondsignupactivity.this, "Please enter passion", Toast.LENGTH_SHORT).show();

        } else {

            dialog = new ACProgressFlower.Builder(Secondsignupactivity.this)
                    .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                    .themeColor(Color.WHITE)

                    .fadeColor(Color.DKGRAY).build();
            dialog.show();
            if (bitmap != null) {
                setImage(bitmap);
            } else {
                bitmap = BitmapFactory.decodeResource(getResources(),
                        R.mipmap.sign_up_default_user_img);
                setImage(bitmap);


            }
            file.saveInBackground(new SaveCallback() {
                public void done(ParseException e) {
                    // If successful add file to user and signUpInBackground
                    if(null == e){
                        signupwidparse(fullname, title, passion);}
                        else {
                        Log.e("GETERROEIMAGE",e.getMessage());
                    }
                }
            });



        }
    }

    private void signupwidparse(String fullname, String title, String passion) {
        ParseUser user = new ParseUser();
        user.setUsername(email);
        user.setPassword(pwd);
        user.setEmail(email);

// other fields can be set just like with ParseObject
        user.put("fullname", fullname);
        user.put("title", title);
        user.put("passion", passion);
        user.put("profileimage", file);
        callparse(user);



        Log.e("PARAMS", user.isAuthenticated() + "");


    }

    private void setImage(Bitmap bitmap) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // Compress image to lower quality scale 1 - 100
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] image = stream.toByteArray();


        // Create the ParseFile
        file = new ParseFile("agefile.jpg", image);

    }

    private void callparse(final ParseUser user) {

        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    // Hooray! Let them use the app now.
                    Toast.makeText(Secondsignupactivity.this, "succes", Toast.LENGTH_SHORT).show();
                    AppConstants.savePreferences(Secondsignupactivity.this, "email", user.getEmail());
                    AppConstants.savePreferences(Secondsignupactivity.this, "name", user.getUsername());
                    AppConstants.savePreferences(Secondsignupactivity.this, "login", "1");

                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Intent intent = new Intent(Secondsignupactivity.this, CreateNjoinworkActivity.class);
                    startActivity(intent);
                } else {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Log.e("MESAGE", e.getMessage());
                    Toast.makeText(Secondsignupactivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo")) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, REQUEST_CAMERA);
                    } else if (userChoosenTask.equals("Choose from Library")) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);//
                        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
                    }
                } else {
                    //code for deny
                }
                break;
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(Secondsignupactivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(Secondsignupactivity.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, REQUEST_CAMERA);
                    }

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);//
                        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
                    }

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    private void findviews() {
        customFonts = new CustomFonts(Secondsignupactivity.this);
        llSignup = (LinearLayout) findViewById(R.id.llSignup);
        text = (TextView) findViewById(R.id.text);
        edt_fullname = (EditText) findViewById(R.id.edt_fullname);
        edt_title = (EditText) findViewById(R.id.edt_title);
        edt_passion = (EditText) findViewById(R.id.edt_passion);
        txtTerm1 = (TextView) findViewById(R.id.txtTerm1);
        txtTerm2 = (TextView) findViewById(R.id.txtTerm2);

        rl_profilePic = (RelativeLayout) findViewById(R.id.rl_profilePic);
        user_image = (ImageView) findViewById(R.id.user_image);
        text.setTypeface(customFonts.HelveticaNeue);
        txtTerm1.setTypeface(customFonts.HelveticaNeueMedium);
        txtTerm2.setTypeface(customFonts.HelveticaNeueMedium);


        edt_title.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS | InputType.TYPE_CLASS_TEXT);
        edt_fullname.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS | InputType.TYPE_CLASS_TEXT);
        edt_passion.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS | InputType.TYPE_CLASS_TEXT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;


        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        user_image.setImageBitmap(thumbnail);
        bitmap = thumbnail;
//        image = BitMapToString(thumbnail);


        final double viewWidthToBitmapWidthRatio = (double) user_image.getWidth() / (double) thumbnail.getWidth();
        user_image.getLayoutParams().height = (int) (thumbnail.getHeight() * viewWidthToBitmapWidthRatio);
    }


    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        bitmap = bm;

        user_image.setImageBitmap(bm);


//        image = BitMapToString(bm);
//        file = new File(image);


        final double viewWidthToBitmapWidthRatio = (double) user_image.getWidth() / (double) bm.getWidth();
        user_image.getLayoutParams().height = (int) (bm.getHeight() * viewWidthToBitmapWidthRatio);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
