package com.samosys.paperai.activity.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.samosys.paperai.R;
import com.samosys.paperai.activity.utils.AppConstants;
import com.samosys.paperai.activity.utils.First_Char_Capital;
import com.samosys.paperai.activity.utils.MarshMallowPermission;
import com.samosys.paperai.activity.utils.NetworkAvailablity;
import com.samosys.paperai.activity.utils.Utility;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import cc.cloudist.acplibrary.ACProgressFlower;

public class EditMyProfileActivity extends AppCompatActivity {
    ImageView my_imgEdit, img_camera;
    EditText edtName, edtRole, edtcommunityrole, edtAdress, edtTitle, edtpassion;
    TextView txtsaveProfle, txtlogout;
    MarshMallowPermission marshMallowPermission;
    Bitmap bitmap = null;
    ParseFile file = null;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_my_profile);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        AppConstants.getTranparentstatusbar(EditMyProfileActivity.this);
        my_imgEdit = (ImageView) findViewById(R.id.my_imgEdit);
        finvoiew();
        if (NetworkAvailablity.chkStatus(EditMyProfileActivity.this)) {


            getuserdata();

        } else {
            Toast.makeText(EditMyProfileActivity.this, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
        }
        img_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (!marshMallowPermission.checkPermissionForExternalStorage()) {
                        marshMallowPermission.requestPermissionForExternalStorage();
                    } else {

                        selectImage();
                    }
                } else {

                    selectImage();
                }
            }
        });
        txtlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                ParseUser currentUser = ParseUser.getCurrentUser();

                AppConstants.ClearPreferences(EditMyProfileActivity.this);
                Intent intent = new Intent(EditMyProfileActivity.this, LoginSignupActivity.class);
                startActivity(intent);
                finish();
            }
        });
        txtsaveProfle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ACProgressFlower acProgressFlower = AppConstants.CustomshowProgressDialog(EditMyProfileActivity.this, "");
                acProgressFlower.show();


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
                        if (null == e) {
                            String uname = edtName.getText().toString();
                            String compantrole = edtRole.getText().toString();
                            String community = edtcommunityrole.getText().toString();
                            String address = edtAdress.getText().toString();
                            String title = edtTitle.getText().toString();
                            String passion = edtpassion.getText().toString();
                            updateprofile(uname, compantrole, community, address, title, passion,file, acProgressFlower);


                        } else {
                            Log.e("GETERROEIMAGE", e.getMessage());
                        }
                    }
                });



            }
        });
    }

    private void setImage(Bitmap bitmap) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // Compress image to lower quality scale 1 - 100
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] image = stream.toByteArray();


        // Create the ParseFile
        file = new ParseFile("file.jpg", image);

    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(EditMyProfileActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(EditMyProfileActivity.this);

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

    private void updateprofile(final String uname, final String compantrole, final String community, final String address, final String title, final String passion, final ParseFile file, final ACProgressFlower acProgressFlower) {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
        query.whereEqualTo("objectId", ParseUser.getCurrentUser().getObjectId());
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    ParseObject person = list.get(0);
                    person.put("fullname", uname);
                    person.put("campanyrole", compantrole);
                    person.put("communityrole", community);
                    person.put("address", address);
                    person.put("title", title);
                    person.put("passion", passion);
                    person.put("profileimage", file);
                    if (NetworkAvailablity.chkStatus(EditMyProfileActivity.this)) {
//                        getprojectlist();

                        if (acProgressFlower.isShowing()) {
                            acProgressFlower.dismiss();
                        }
                        Toast.makeText(EditMyProfileActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                        finish();


                    } else {
                        Toast.makeText(EditMyProfileActivity.this, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                    }

                    person.saveInBackground();
                } else {
                    if (acProgressFlower.isShowing()) {
                        acProgressFlower.dismiss();
                    }
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });
    }

    private void finvoiew() {
        edtName = (EditText) findViewById(R.id.edtName);
        edtRole = (EditText) findViewById(R.id.edtRole);
        edtcommunityrole = (EditText) findViewById(R.id.edtcommunityrole);
        edtAdress = (EditText) findViewById(R.id.edtAdress);
        edtTitle = (EditText) findViewById(R.id.edtTitle);
        img_camera = (ImageView) findViewById(R.id.img_camera);
        edtpassion = (EditText) findViewById(R.id.edtpassion);
        txtsaveProfle = (TextView) findViewById(R.id.txtsaveProfle);
        txtlogout = (TextView) findViewById(R.id.txtlogout);
        marshMallowPermission = new MarshMallowPermission(EditMyProfileActivity.this);
    }


    private void getuserdata() {

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("objectId", ParseUser.getCurrentUser().getObjectId());
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); i++) {
                        String objectId = objects.get(i).getObjectId();
                        String username = objects.get(i).getString("username");
                        String email = objects.get(i).getString("email");
                        Log.e("EMAIL_USER", email + ">>" + username);

                        String updatedAt = objects.get(i).getUpdatedAt().toString();
                        String createdAt = objects.get(i).getCreatedAt().toString();
                        String passion = objects.get(i).getString("passion");
                        ParseFile profileimage = (ParseFile) objects.get(i).get("profileimage");
                        String title = objects.get(i).getString("title");
                        String fullname = objects.get(i).getString("fullname");
                        String campanyrole = objects.get(i).getString("campanyrole");
                        String communityrole = objects.get(i).getString("communityrole");
                        String address = objects.get(i).getString("address");
                        edtName.setText(First_Char_Capital.capitalizeString(fullname));
                        edtRole.setText(campanyrole);
                        edtcommunityrole.setText(communityrole);
                        edtAdress.setText(address);
                        edtTitle.setText(title);
                        edtpassion.setText(passion);
                        profileimage.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                Bitmap bmp = BitmapFactory
                                        .decodeByteArray(
                                                data, 0,
                                                data.length);

                                // initialize
                                my_imgEdit.setImageBitmap(bmp);
                            }
                        });
                    }
                } else {
                    Toast.makeText(EditMyProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


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

        my_imgEdit.setImageBitmap(thumbnail);
        bitmap = thumbnail;
//        image = BitMapToString(thumbnail);


        final double viewWidthToBitmapWidthRatio = (double) my_imgEdit.getWidth() / (double) thumbnail.getWidth();
        my_imgEdit.getLayoutParams().height = (int) (thumbnail.getHeight() * viewWidthToBitmapWidthRatio);
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

        my_imgEdit.setImageBitmap(bm);


//        image = BitMapToString(bm);
//        file = new File(image);


        final double viewWidthToBitmapWidthRatio = (double) my_imgEdit.getWidth() / (double) bm.getWidth();
        my_imgEdit.getLayoutParams().height = (int) (bm.getHeight() * viewWidthToBitmapWidthRatio);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
