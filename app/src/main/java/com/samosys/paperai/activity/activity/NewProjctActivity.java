package com.samosys.paperai.activity.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.samosys.paperai.R;
import com.samosys.paperai.activity.utils.AppConstants;
import com.samosys.paperai.activity.utils.AwesomeToggle;
import com.samosys.paperai.activity.utils.MarshMallowPermission;
import com.samosys.paperai.activity.utils.SwitchButton;
import com.samosys.paperai.activity.utils.Utility;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class NewProjctActivity extends AppCompatActivity {
    private  EditText edt_workspace, edt_mission;
    private  LinearLayout llcreate;
    private  String id="", togg="1";;
    private SwitchButton awesomeToggle;
    private  boolean toggle=true;
    private LinearLayout rl_toggle;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private  ImageView img_project;
    private String userChoosenTask;
    private  Bitmap bitmap = null;
    private  ParseFile file = null;
    private  MarshMallowPermission marshMallowPermission;
    private ACProgressFlower dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_projct);
        marshMallowPermission=new MarshMallowPermission(NewProjctActivity.this);
        AppConstants.getTranparentstatusbar(NewProjctActivity.this);
        edt_workspace = (EditText) findViewById(R.id.edt_projectname);
        edt_mission = (EditText) findViewById(R.id.edt_objective);
        llcreate = (LinearLayout) findViewById(R.id.llcreate);
        awesomeToggle=(SwitchButton)findViewById(R.id.awesomeToggle);
        rl_toggle=(LinearLayout)findViewById(R.id.rl_toggle);
        img_project=(ImageView)findViewById(R.id.img_project);
        id = getIntent().getStringExtra("id");
        llcreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new ACProgressFlower.Builder(NewProjctActivity.this)
                        .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                        .themeColor(Color.WHITE)

                        .fadeColor(Color.DKGRAY).build();
                dialog.show();
                  //ProgressDialog dialog = AppConstants.showProgressDialog(NewProjctActivity.this, "Please wait...");
                  String workspace = edt_workspace.getText().toString();
                  String mission = edt_mission.getText().toString();

                if (workspace.equals("")){
                    Toast.makeText(NewProjctActivity.this, "Please enter project name", Toast.LENGTH_SHORT).show();
                }else {
                validation(workspace,mission);}
            }
        });
        awesomeToggle.setChecked(true);
        awesomeToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){

                    toggle=true;
                    togg="1";
                    Log.e("toggle>>",">"+togg);
                }else {
                    toggle=false;
                    togg="2";
                    Log.e("toggle22>>","22>"+togg);

                }
            }
        });



        img_project.setOnClickListener(new View.OnClickListener() {
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

        img_project.setImageBitmap(thumbnail);
        bitmap = thumbnail;

        final double viewWidthToBitmapWidthRatio = (double) img_project.getWidth() / (double) thumbnail.getWidth();
        img_project.getLayoutParams().height = (int) (thumbnail.getHeight() * viewWidthToBitmapWidthRatio);


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

        img_project.setImageBitmap(bm);




        final double viewWidthToBitmapWidthRatio = (double) img_project.getWidth() / (double) bm.getWidth();
        img_project.getLayoutParams().height = (int) (bm.getHeight() * viewWidthToBitmapWidthRatio);

    }
    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(NewProjctActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(NewProjctActivity.this);

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

    private void setImage(Bitmap bitmap) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // Compress image to lower quality scale 1 - 100
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] image = stream.toByteArray();


        // Create the ParseFile
        file = new ParseFile("agefile.jpg", image);

    }


    private void validation( final String workspace, final String mission) {



        if (workspace.equals("") || workspace == null) {
            Toast.makeText(this, "Please enter project name", Toast.LENGTH_SHORT).show();
        } else  {
            if (bitmap != null) {
                setImage(bitmap);
            } else {
                bitmap = BitmapFactory.decodeResource(getResources(),
                        R.mipmap.sign_up_project);
                setImage(bitmap);
            }
            file.saveInBackground(new SaveCallback() {
                public void done(ParseException e) {
                    // If successful add file to user and signUpInBackground
                    if (null == e) {

                        ParseObject gameScore = new ParseObject("Project");
                        //gameScore.put("user", ParseUser.getCurrentUser().getObjectId());
                        gameScore.put("user", ParseObject.createWithoutData("_User", ParseUser.getCurrentUser().getObjectId()));
                        gameScore.put("name", workspace);
                        gameScore.put("workspace", ParseObject.createWithoutData("WorkSpace", id));
                        gameScore.put("objective", mission);
                        gameScore.put("type", togg);
                        gameScore.put("archive", "0");
                        gameScore.put("image", file);
                        gameScore.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                                if (e == null) {
                                    AppConstants.savePreferences(NewProjctActivity.this, "workid",id);
                                    Intent intent = new Intent(NewProjctActivity.this, HomeFeedActivity.class);
                                    intent.putExtra("id", "");
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Log.e("ERRROr",e.getMessage());
                                    Toast.makeText(NewProjctActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            });







        }
    }
}
