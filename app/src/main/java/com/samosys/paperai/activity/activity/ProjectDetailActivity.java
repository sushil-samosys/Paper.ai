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
import com.samosys.paperai.activity.utils.SwitchButton;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ProjectDetailActivity extends AppCompatActivity {
    MarshMallowPermission marshMallowPermission;
    private String proID = "";
    private ImageView imgProject, img_camera, img_project_back;
    private TextView txtproject_header, txt_followrs, txt_member, txt_notification, txt_public, txt_cat, txt_cat_value, txt_object, txt_project;
    private int img_height, img_width;
    private EditText txt_projct_value, txt_objective_value;
    private CustomFonts customFonts;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1, SELECT_VIDEO = 3;
    private Bitmap bitmap = null;
    private SwitchButton awesomeToggle;
    private LinearLayout ll_save_project;
    private ParseFile file = null, imageFile = null;
    private String type = "";
    private RelativeLayout rlProjectArchive;
//ArchiveProjectActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_detail);
        AppConstants.getstatusbar(ProjectDetailActivity.this);
        AppConstants.hideKeyboard(ProjectDetailActivity.this);
        proID = getIntent().getStringExtra("id");
        findview();
        BitmapDrawable bd = (BitmapDrawable) ProjectDetailActivity.this.getResources().getDrawable(R.mipmap.img_feed_center_1);
        img_height = bd.getBitmap().getHeight() + (img_height);
        img_width = bd.getBitmap().getWidth() + (img_width);
        if (NetworkAvailablity.chkStatus(ProjectDetailActivity.this)) {
            getProject();

        } else {
            Toast.makeText(ProjectDetailActivity.this, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
        }


        img_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askForCameraPermission();
            }
        });

        ll_save_project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.hideKeyboard(ProjectDetailActivity.this);
                final ProgressDialog progressDialog = AppConstants.showProgressDialog(ProjectDetailActivity.this, "Please wait...");
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
                            callupdateproject(progressDialog);
                        }
                    }
                });


            }
        });
        img_project_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        rlProjectArchive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProjectDetailActivity.this, ArchiveProjectActivity.class);
                intent.putExtra("id",proID);
                startActivity(intent);
            }
        });
    }

    private void callupdateproject(final ProgressDialog progressDialog) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Project");
        query.whereEqualTo("objectId", proID);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {


                    ParseObject person = list.get(0);
                    person.put("objective", txt_objective_value.getText().toString());
                    person.put("image", file);
                    person.put("name", txt_projct_value.getText().toString());
                    person.put("type", type);
                    person.saveInBackground();
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    finish();
                    Toast.makeText(ProjectDetailActivity.this, "Saved successfully", Toast.LENGTH_SHORT).show();


                } else {
                    Log.e("score", "Error: " + e.getMessage());
                }
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

        imgProject.setImageBitmap(thumbnail);
        bitmap = thumbnail;

        final double viewWidthToBitmapWidthRatio = (double) imgProject.getWidth() / (double) thumbnail.getWidth();
        imgProject.getLayoutParams().height = img_height;


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

        imgProject.setImageBitmap(bm);


//        image = BitMapToString(bm);

//        file = new File(image);


        final double viewWidthToBitmapWidthRatio = (double) imgProject.getWidth() / (double) bm.getWidth();
        imgProject.getLayoutParams().height = (int) (bm.getHeight() * viewWidthToBitmapWidthRatio);

    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(ProjectDetailActivity.this);
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

    private void findview() {
        marshMallowPermission = new MarshMallowPermission(ProjectDetailActivity.this);
        customFonts = new CustomFonts(ProjectDetailActivity.this);
        imgProject = (ImageView) findViewById(R.id.imgProject);
        img_camera = (ImageView) findViewById(R.id.img_camera);
        img_project_back = (ImageView) findViewById(R.id.img_project_back);
        txt_projct_value = (EditText) findViewById(R.id.txt_projct_value);
        txt_objective_value = (EditText) findViewById(R.id.txt_objective_value);
        txtproject_header = (TextView) findViewById(R.id.txtproject_header);
        txt_followrs = (TextView) findViewById(R.id.txt_followrs);
        txt_member = (TextView) findViewById(R.id.txt_member);
        txt_notification = (TextView) findViewById(R.id.txt_notification);
        txt_project = (TextView) findViewById(R.id.txt_project);
        txt_public = (TextView) findViewById(R.id.txt_public);
        txt_object = (TextView) findViewById(R.id.txt_object);
        txt_cat = (TextView) findViewById(R.id.txt_cat);
        txt_cat_value = (TextView) findViewById(R.id.txt_cat_value);
        awesomeToggle = (SwitchButton) findViewById(R.id.awesomeToggle);
        ll_save_project = (LinearLayout) findViewById(R.id.ll_save_project);
        rlProjectArchive = (RelativeLayout) findViewById(R.id.rlProjectArchive);


        txtproject_header.setTypeface(customFonts.CabinBold);
        txt_project.setTypeface(customFonts.CabinBold);
        txt_public.setTypeface(customFonts.CabinBold);
        txt_cat.setTypeface(customFonts.CabinBold);
        txt_object.setTypeface(customFonts.CabinBold);
        txt_objective_value.setTypeface(customFonts.CabinRegular);
        txt_projct_value.setTypeface(customFonts.CabinRegular);
        txt_cat_value.setTypeface(customFonts.CabinRegular);
        txt_member.setTypeface(customFonts.CabinRegular);
        txt_followrs.setTypeface(customFonts.CabinRegular);
        txt_notification.setTypeface(customFonts.CabinRegular);
    }

    private void getProject() {
        // Toast.makeText(this, s, Toast.LENGTH_SHORT).show();


        final ProgressDialog dialog = AppConstants.showProgressDialog(ProjectDetailActivity.this, "Loading...");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Project");
        query.whereEqualTo("objectId", proID);

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects1, ParseException e) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }


                if (e == null) {


                    if (objects1.size() > 0) {

                        for (int i = 0; i < objects1.size(); i++) {


                            type = objects1.get(i).getString("type");
                            String updatedAt = objects1.get(i).getUpdatedAt().toString();
                            String projectname = objects1.get(i).getString("name");
                            String objective = objects1.get(i).getString("objective");

                            String createdAt = objects1.get(i).getCreatedAt().toString();

                            imageFile = (ParseFile) objects1.get(i).get("image");

                            String post_image = imageFile.getUrl();
                            String user_name = objects1.get(i).getString("user_name");
                            Log.e("imageFile", imageFile + "");
                            txt_projct_value.setText(First_Char_Capital.capitalizeString(projectname));
                            txtproject_header.setText(First_Char_Capital.capitalizeString(projectname));
                            txt_objective_value.setText(objective);
                            if (type.equals("1")) {
                                awesomeToggle.setChecked(true);
                            } else {
                                awesomeToggle.setChecked(false);
                            }
                            if (post_image != null) {


                                Picasso.with(ProjectDetailActivity.this)
                                        .load(post_image)
                                        .fit()
                                        .centerCrop()

                                        .into(imgProject);
                            }
                            imgProject.getLayoutParams().height = img_height;


                        }
                    }


                } else {
                    Toast.makeText(ProjectDetailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    Log.e("else", e.getMessage());
                    // error
                }


            }
        });
    }
}
