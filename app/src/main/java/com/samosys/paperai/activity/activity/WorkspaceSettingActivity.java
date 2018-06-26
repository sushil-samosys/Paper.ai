package com.samosys.paperai.activity.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.samosys.paperai.R;
import com.samosys.paperai.activity.utils.AppConstants;
import com.samosys.paperai.activity.utils.CustomFonts;
import com.samosys.paperai.activity.utils.First_Char_Capital;
import com.samosys.paperai.activity.utils.MarshMallowPermission;
import com.samosys.paperai.activity.utils.NetworkAvailablity;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class WorkspaceSettingActivity extends AppCompatActivity {
    MarshMallowPermission marshMallowPermission;
    RelativeLayout rlArchive;
    private TextView txt_workname_header, txt_workName,
            txt_mission, txt_missionurl, txt_missionurl_value, txt_followrs, txt_member, txt_notification;
    private ImageView img_work_space, img_work_space_back, img_camera;
    private int img_height, img_width;
    private EditText txt_workName_value, txt_mission_value;
    private CustomFonts customFonts;
    private LinearLayout ll_save;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1, SELECT_VIDEO = 3;
    private Bitmap bitmap = null;
    private ParseFile file = null, imageFile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workspace_setting);
        AppConstants.getstatusbar(WorkspaceSettingActivity.this);
        AppConstants.hideKeyboard(WorkspaceSettingActivity.this);
        findview();
        BitmapDrawable bd = (BitmapDrawable) WorkspaceSettingActivity.this.getResources().getDrawable(R.mipmap.img_feed_center_1);
        img_height = bd.getBitmap().getHeight() + (img_height);
        img_width = bd.getBitmap().getWidth() + (img_width);
        if (NetworkAvailablity.chkStatus(WorkspaceSettingActivity.this)) {
            getworkspace();

        } else {
            Toast.makeText(WorkspaceSettingActivity.this, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
        }
        img_work_space_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ll_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.hideKeyboard(WorkspaceSettingActivity.this);
                final ProgressDialog progressDialog = AppConstants.showProgressDialog(WorkspaceSettingActivity.this, "Please wait...");
                if (bitmap != null) {
                    setImage(bitmap);
                } else {
//                    bitmap = BitmapFactory.decodeResource(getResources(),
//                            R.mipmap.sign_up_workspace);
//                    setImage(bitmap);
                    file = imageFile;
                }

                file.saveInBackground(new SaveCallback() {
                    public void done(ParseException e) {
                        // If successful add file to user and signUpInBackground
                        if (null == e) {
                            callupdateapi(progressDialog);
                        }
                    }
                });


            }
        });
        rlArchive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WorkspaceSettingActivity.this, ArchiveWorkspaceActivity.class);
                startActivity(intent);
            }
        });

        img_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askForCameraPermission();
            }
        });
    }

    private void setImage(Bitmap bitmap) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // Compress image to lower quality scale 1 - 100
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] image = stream.toByteArray();


        // Create the ParseFile
        file = new ParseFile("agefile.jpg", image);

    }


    public void askForCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (!marshMallowPermission.checkPermissionForExternalStorage()) {
                marshMallowPermission.requestPermissionForExternalStorage();
            } else {
                //  showDialog();
                selectImage();
            }
        } else {
            //  showDialog();
            selectImage();
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(WorkspaceSettingActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

//                    File file = getOutputMediaFile(1);
//
//
//                    // String extension = android.webkit.MimeTypeMap.getFileExtensionFromUrl(Uri.fromF‌​ile(file).toString()‌​);
//
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                        //   String extension = android.webkit.MimeTypeMap.getFileExtensionFromUrl(file.toString()‌​);
////                        intent.setDataAndType(picUri, type);
//                        picUri = FileProvider.getUriForFile(WorkspaceSettingActivity.this, WorkspaceSettingActivity.this.getApplicationContext().getPackageName() + ".provider", file);
//                        //picUri=FileProvider.getUriForFile(AddPhotoVideo.this, BuildConfig.APPLICATION_ID+".provider", file);
//                        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                        intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//
//
//                    } else {
//                        picUri = Uri.fromFile(file); // create
//
//                    }
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
                    // set the image file
                    startActivityForResult(intent, REQUEST_CAMERA);


                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                // This method is call for getting image from gallary
                onSelectFromGalleryResult(data);
            } else if (requestCode == REQUEST_CAMERA) {


// This method is call for capturing image from camera
                onCaptureImageResult(data);


            }


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

        img_work_space.setImageBitmap(thumbnail);
        bitmap = thumbnail;

        final double viewWidthToBitmapWidthRatio = (double) img_work_space.getWidth() / (double) thumbnail.getWidth();
        img_work_space.getLayoutParams().height = img_height;


    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

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

        img_work_space.setImageBitmap(bm);


//        image = BitMapToString(bm);

//        file = new File(image);


        final double viewWidthToBitmapWidthRatio = (double) img_work_space.getWidth() / (double) bm.getWidth();
        img_work_space.getLayoutParams().height = (int) (bm.getHeight() * viewWidthToBitmapWidthRatio);

    }

    private File getOutputMediaFile(int type) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyApplication");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == 1) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".png");
        } else {
            return null;
        }

        return mediaFile;
    }


    private void callupdateapi(final ProgressDialog progressDialog) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("WorkSpace");
        query.whereEqualTo("objectId", AppConstants.loadPreferences(WorkspaceSettingActivity.this, "workid"));
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {

                    ParseObject person = list.get(0);
                    person.put("mission", txt_mission_value.getText().toString());
                    person.put("image", file);
                    person.put("workspace_name", txt_workName_value.getText().toString());
                    person.saveInBackground();
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    Toast.makeText(WorkspaceSettingActivity.this, "Saved successfully", Toast.LENGTH_SHORT).show();


                } else {
                    Log.e("score", "Error: " + e.getMessage());
                }
            }
        });
    }

    private void findview() {
        marshMallowPermission = new MarshMallowPermission(WorkspaceSettingActivity.this);
        customFonts = new CustomFonts(WorkspaceSettingActivity.this);
        txt_workname_header = (TextView) findViewById(R.id.txt_workname_header);
        txt_workName = (TextView) findViewById(R.id.txt_workName);
        txt_mission = (TextView) findViewById(R.id.txt_mission);
        txt_followrs = (TextView) findViewById(R.id.txt_followrs);
        txt_member = (TextView) findViewById(R.id.txt_member);
        txt_notification = (TextView) findViewById(R.id.txt_notification);
        txt_missionurl_value = (TextView) findViewById(R.id.txt_missionurl_value);
        txt_missionurl = (TextView) findViewById(R.id.txt_missionurl);
        txt_workName_value = (EditText) findViewById(R.id.txt_workName_value);
        txt_mission_value = (EditText) findViewById(R.id.txt_mission_value);
        img_work_space_back = (ImageView) findViewById(R.id.img_work_space_back);
        img_work_space = (ImageView) findViewById(R.id.img_work_space);
        img_camera = (ImageView) findViewById(R.id.img_camera);
        ll_save = (LinearLayout) findViewById(R.id.ll_save);
        rlArchive = (RelativeLayout) findViewById(R.id.rlArchive);

        txt_workname_header.setTypeface(customFonts.CabinBold);
        txt_workName.setTypeface(customFonts.CabinBold);
        txt_missionurl.setTypeface(customFonts.CabinBold);
        txt_mission.setTypeface(customFonts.CabinBold);
        txt_workName_value.setTypeface(customFonts.CabinRegular);
        txt_mission_value.setTypeface(customFonts.CabinRegular);
        txt_missionurl_value.setTypeface(customFonts.CabinRegular);
        txt_member.setTypeface(customFonts.CabinRegular);
        txt_followrs.setTypeface(customFonts.CabinRegular);
        txt_notification.setTypeface(customFonts.CabinRegular);

    }

    private void getworkspace() {
        // Toast.makeText(this, s, Toast.LENGTH_SHORT).show();


        final ProgressDialog dialog = AppConstants.showProgressDialog(WorkspaceSettingActivity.this, "Loading...");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("WorkSpace");
        query.whereEqualTo("objectId", AppConstants.loadPreferences(WorkspaceSettingActivity.this, "workid"));

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects1, ParseException e) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }


                if (e == null) {


                    if (objects1.size() > 0) {

                        for (int i = 0; i < objects1.size(); i++) {


                            String mission = objects1.get(i).getString("mission");
                            String updatedAt = objects1.get(i).getUpdatedAt().toString();
                            String workspace_n = objects1.get(i).getString("workspace_name");
                            String workspace_url = objects1.get(i).getString("workspace_url");

                            String createdAt = objects1.get(i).getCreatedAt().toString();
                            String image = objects1.get(i).getString("image");
                            imageFile = (ParseFile) objects1.get(i).get("image");

                            String post_image = imageFile.getUrl();
                            String user_name = objects1.get(i).getString("user_name");
                            Log.e("imageFile_mission", mission + "");
                            AppConstants.savePreferences(WorkspaceSettingActivity.this, "workname", workspace_n);
                            txt_workname_header.setText(First_Char_Capital.capitalizeString(workspace_n));
                            txt_mission_value.setText(First_Char_Capital.capitalizeString(mission));
                            txt_missionurl_value.setText(workspace_url);
                            txt_workName_value.setText(First_Char_Capital.capitalizeString(workspace_n));
                            if (imageFile != null) {


                                Picasso.with(WorkspaceSettingActivity.this)
                                        .load(post_image)
                                        .fit()
                                        .centerCrop()

                                        .into(img_work_space);
                            }
                            img_work_space.getLayoutParams().height = img_height;


                        }
                    }


                } else {
                    Toast.makeText(WorkspaceSettingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    Log.e("else", e.getMessage());
                    // error
                }


            }
        });
    }
}
