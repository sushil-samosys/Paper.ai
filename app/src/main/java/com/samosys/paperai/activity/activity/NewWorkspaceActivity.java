package com.samosys.paperai.activity.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.samosys.paperai.R;
import com.samosys.paperai.activity.utils.AppConstants;
import com.samosys.paperai.activity.utils.NetworkAvailablity;
import com.samosys.paperai.activity.utils.Utility;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class NewWorkspaceActivity extends AppCompatActivity {
    EditText edt_workspace, edt_mission, edt_workspace_url;
    LinearLayout llcreate;
    String mainurl = "";
    ImageView img_work_space;
    ArrayList<String> mylist;

    Bitmap bitmap = null;
    ParseFile file = null;
    private String userChoosenTask;
    private  ACProgressFlower dialogProgred;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_workspace);
        AppConstants.getTranparentstatusbar(NewWorkspaceActivity.this);
        img_work_space = (ImageView) findViewById(R.id.img_work_space);
        edt_workspace = (EditText) findViewById(R.id.edt_workspace);
        edt_mission = (EditText) findViewById(R.id.edt_mission);
        edt_workspace_url = (EditText) findViewById(R.id.edt_workspacurl);
//        edt_mission.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS | InputType.TYPE_CLASS_TEXT);
        edt_workspace.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS | InputType.TYPE_CLASS_TEXT);
        llcreate = (LinearLayout) findViewById(R.id.llcreate);
        mylist = new ArrayList<>();
        llcreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogProgred = new ACProgressFlower.Builder(NewWorkspaceActivity.this)
                        .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                        .themeColor(Color.WHITE)

                        .fadeColor(Color.DKGRAY).build();
                dialogProgred.show();


                 validation();
            }
        });

        edt_workspace.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {

                    String url = edt_workspace.getText().toString().toLowerCase().trim() + "workspace";
                    url = url.replaceAll(" ", "");
                    edt_workspace_url.setText(url);
                    mainurl = url.toLowerCase() + ".papr.ai";


                }
            }
        });

        img_work_space.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (NetworkAvailablity.chkStatus(NewWorkspaceActivity.this)) {
            getworkspacelist();
        } else {
            Toast.makeText(NewWorkspaceActivity.this, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
        }


    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(NewWorkspaceActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(NewWorkspaceActivity.this);

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
                    dialogProgred.dismiss();
                }
            }
        });
        builder.show();
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

        img_work_space.setImageBitmap(thumbnail);
        bitmap = thumbnail;

        final double viewWidthToBitmapWidthRatio = (double) img_work_space.getWidth() / (double) thumbnail.getWidth();
        img_work_space.getLayoutParams().height = (int) (thumbnail.getHeight() * viewWidthToBitmapWidthRatio);


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

        img_work_space.setImageBitmap(bm);


//        image = BitMapToString(bm);

//        file = new File(image);


        final double viewWidthToBitmapWidthRatio = (double) img_work_space.getWidth() / (double) bm.getWidth();
        img_work_space.getLayoutParams().height = (int) (bm.getHeight() * viewWidthToBitmapWidthRatio);

    }

    private void setImage(Bitmap bitmap) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // Compress image to lower quality scale 1 - 100
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] image = stream.toByteArray();


        // Create the ParseFile
        file = new ParseFile("agefile.jpg", image);

    }


    private void getworkspacelist() {

      //  final ProgressDialog dialog = AppConstants.showProgressDialog(NewWorkspaceActivity.this, "Loading...");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("WorkSpace");
        //  query.whereNotEqualTo("wokspace_url", url );
        // query.whereNotEqualTo("user", ParseUser.getCurrentUser().getObjectId());
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {

                if (e == null) {
                    for (int i = 0; i < objects.size(); i++) {
                        String url = objects.get(i).getString("workspace_url");
                        mylist.add(url);
                    }

                    Log.e("MYLIST", mylist.toString());
                } else {
                    if (dialogProgred.isShowing()) {
                        dialogProgred.dismiss();
                    }
                    //Toast.makeText(NewWorkspaceActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    Log.e("else", e.getMessage());
                    Toast.makeText(NewWorkspaceActivity.this, "Wrokspace url already exist", Toast.LENGTH_SHORT).show();
                    // error
                }
            }
        });
    }

    private void validation( ) {

        final String workspace = edt_workspace.getText().toString();
        final String mission = edt_mission.getText().toString();


        if (workspace.equals("") || workspace == null) {
            Toast.makeText(this, "Please enter workspace name", Toast.LENGTH_SHORT).show();
        } else if (mainurl.equals("") || mainurl == null) {
            Toast.makeText(this, "Please enter wrokspace url", Toast.LENGTH_SHORT).show();
        } else if (mylist.contains(mainurl)) {
            Toast.makeText(this, "Workspace url already exist", Toast.LENGTH_SHORT).show();
        } else {
            if (bitmap != null) {
                setImage(bitmap);
            } else {
                bitmap = BitmapFactory.decodeResource(getResources(),
                        R.mipmap.sign_up_workspace);
                setImage(bitmap);
            }
            file.saveInBackground(new SaveCallback() {
                public void done(ParseException e) {
                    // If successful add file to user and signUpInBackground
                    if (null == e) {
                        createworkspace(workspace, mission);
                    }else {
                        Log.e("ImageERROR", "" + e.getMessage());
                    }
                }
            });

        }
    }

    private void createworkspace(String workspace, String mission) {


        final ParseObject gameScore = new ParseObject("WorkSpace");
        gameScore.put("user", ParseObject.createWithoutData("_User", ParseUser.getCurrentUser().getObjectId()));
//        gameScore.put("user", ParseUser.getCurrentUser().getObjectId());
        gameScore.put("mission", mission);
        gameScore.put("workspace_name", workspace);
        gameScore.put("workspace_url", mainurl);
        gameScore.put("image", file);
        Log.e("WORKPARAM", "" + gameScore);

        gameScore.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                if (e == null) {
                    Log.e("MYOBJECT", "" + gameScore.getObjectId());
                    Log.e("MYOBJECT11", "" + gameScore.getString("workspace_url"));
                    Log.e("MYOBJECT22", "" + gameScore.getString("workspace_name"));
                    AppConstants.savePreferences(NewWorkspaceActivity.this, "workid", gameScore.getObjectId());
                    AppConstants.savePreferences(NewWorkspaceActivity.this, "workname", gameScore.getString("workspace_url"));

                    creategeneral(gameScore.getObjectId());


//                    Intent intent = new Intent(NewWorkspaceActivity.this, InviteMemberActivity.class);
//                    intent.putExtra("id", gameScore.getObjectId());
//                    startActivity(intent);
                } else {
                    Log.e("WROKSOACEERROR", "" + e.getMessage());
                    Toast.makeText(NewWorkspaceActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void createmynotes(final String objectId) {
        ParseObject gameScore = new ParseObject("Project");
        //gameScore.put("user", ParseUser.getCurrentUser().getObjectId());
        gameScore.put("user", ParseObject.createWithoutData("_User", ParseUser.getCurrentUser().getObjectId()));
        gameScore.put("name", "My Notes");
        gameScore.put("workspace", ParseObject.createWithoutData("WorkSpace", objectId));
        gameScore.put("objective", "");
        gameScore.put("type", "2");
        gameScore.put("default", "1");
        gameScore.put("image", file);
        gameScore.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                if (e == null) {
                    Intent intent = new Intent(NewWorkspaceActivity.this, InviteMemberActivity.class);
                    intent.putExtra("id", objectId);
                    startActivity(intent);
                    finish();
                } else {
                    Log.e("ERRROr", e.getMessage());
                    Toast.makeText(NewWorkspaceActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                if (dialogProgred.isShowing()) {
                    dialogProgred.dismiss();
                }
            }
        });
    }

    private void creategeneral(final String objectId) {
        final ParseObject gameScore = new ParseObject("Project");
        //gameScore.put("user", ParseUser.getCurrentUser().getObjectId());
        gameScore.put("user", ParseObject.createWithoutData("_User", ParseUser.getCurrentUser().getObjectId()));
        gameScore.put("name", "General");
        gameScore.put("workspace", ParseObject.createWithoutData("WorkSpace", objectId));
        gameScore.put("objective", "");
        gameScore.put("default", "1");
        gameScore.put("type", "1");
        gameScore.put("image", file);
        gameScore.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
//                if (dialog.isShowing()) {
//                    dialog.dismiss();
//                }
                if (e == null) {
//                    Intent intent = new Intent(NewWorkspaceActivity.this, HomeFeedActivity.class);
//                    startActivity(intent);
//                    finish();
                    createmynotes(objectId);
                } else {
                    Log.e("ERRROr", e.getMessage());
                    Toast.makeText(NewWorkspaceActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
