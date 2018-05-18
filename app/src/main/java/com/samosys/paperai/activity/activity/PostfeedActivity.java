package com.samosys.paperai.activity.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.samosys.paperai.R;
import com.samosys.paperai.activity.utils.AppConstants;
import com.samosys.paperai.activity.utils.First_Char_Capital;
import com.samosys.paperai.activity.utils.HeaderView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class PostfeedActivity extends AppCompatActivity  {


//https://github.com/lgvalle/Material-Animations
    ParseFile parseFile = null;
    EditText caption_txt;
    TextView btn_sendPost;
    private String strFile="";
    private RelativeLayout RL_imagefeed;
    private AQuery aq;
    private ImageView imgCapturedimage,btn_fullscren;
    private TextView work_name, txt_text;
    private boolean isHideToolbarView = false;

    View myView;
    boolean isUp=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppConstants.getstatusbar(PostfeedActivity.this);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_postfeed);
        aq = new AQuery(PostfeedActivity.this);

        caption_txt = (EditText) findViewById(R.id.caption_txt);
        work_name = (TextView) findViewById(R.id.workname);
        btn_sendPost = (TextView) findViewById(R.id.btn_sendPost);
        imgCapturedimage = (ImageView) findViewById(R.id.imagesPost);
        btn_fullscren = (ImageView) findViewById(R.id.btn_fullscren);
        RL_imagefeed = (RelativeLayout) findViewById(R.id.RL_imagefeed);

        txt_text = (TextView) findViewById(R.id.txt_text);

        work_name.setText(First_Char_Capital.capitalizeString(AppConstants.loadPreferences(PostfeedActivity.this, "workname")));
        strFile = getIntent().getStringExtra("file");
        Log.e("strFile",strFile);
        if (!strFile.equals("")){
        setImage(strFile);}else {
            RL_imagefeed.setVisibility(View.GONE);
        }

        btn_sendPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!strFile.equals("")) {
                    File newFile = new File(strFile);
                    Uri selectedImage = Uri.fromFile(newFile);


                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(PostfeedActivity.this.getContentResolver(), selectedImage);

                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        // Compress image to lower quality scale 1 - 100
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
                        byte[] image = stream.toByteArray();


                        // Create the ParseFile

                        parseFile = new ParseFile("postfile.jpg", image);
                        createPostImage(parseFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    createPostImage(parseFile);
                }

            }
        });

        btn_fullscren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onSlideViewButtonClick(v);

            }
        });
    }

    public void onSlideViewButtonClick(View view) {
        if (isUp) {
            slideDown();
           // myButton.setText("Slide up");
        } else {
            slideUp();
          //  myButton.setText("Slide down");
        }
        isUp = !isUp;
//        imgCapturedimage.setVisibility(View.GONE);
    }
    public void slideUp(){
//        imgCapturedimage.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                imgCapturedimage.getHeight(),                 // fromXDelta
                0,                 // toXDelta
               0,  // fromYDelta
                0);                // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        imgCapturedimage.startAnimation(animate);
    }

    // slide the view from its current position to below itself
    public void slideDown(){
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                imgCapturedimage.getHeight(),                 // toXDelta
                0,                 // fromYDelta
                0); // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        imgCapturedimage.startAnimation(animate);
    }

    private void createPostImage(ParseFile parseFile) {

        final ProgressDialog dialog = AppConstants.showProgressDialog(PostfeedActivity.this, "Please wait...");

        ParseObject gameScore = new ParseObject("Post");
        gameScore.put("user", ParseObject.createWithoutData("_User", ParseUser.getCurrentUser().getObjectId()));
//
        gameScore.put("text", caption_txt.getText().toString());
        gameScore.put("worksapce", ParseObject.createWithoutData("WorkSpace", AppConstants.loadPreferences(PostfeedActivity.this, "workid")));
        gameScore.put("project", ParseObject.createWithoutData("Project", AppConstants.loadPreferences(PostfeedActivity.this, "projectID")));
        if (parseFile != null) {
            gameScore.put("postImage", parseFile);
        }

        gameScore.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (e == null) {
                    Toast.makeText(PostfeedActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PostfeedActivity.this, HomeFeedActivity.class);
                    finish();
                    startActivity(intent);

                } else {

                    Toast.makeText(PostfeedActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void setImage(String strFile) {
        File newFile = new File(strFile);

        File file = new File(newFile.getAbsolutePath());
        if (file.exists()) {
            BitmapAjaxCallback cb = new BitmapAjaxCallback();
            cb.targetWidth(500).rotate(true);
            aq.id(imgCapturedimage).image(newFile, false, 500, cb);
            //aq.id(imgCapturedimage).image(new File(listitem.get(position).getPost_image()), false, 500, cb);
        } else {

            if (newFile.exists()) {
                aq.ajax(newFile.getAbsolutePath(), File.class, new AjaxCallback<File>() {
                    @Override
                    public void callback(String url, File bm, AjaxStatus status) {

                        if (bm != null) {
                            BitmapAjaxCallback cb = new BitmapAjaxCallback();
                            cb.targetWidth(500).rotate(true);
                            aq.id(imgCapturedimage).image(bm, true, 500, cb);
                        } else {
                            imgCapturedimage.setImageResource(R.mipmap.sign_up_project);
                        }
                    }
                });
            } else {
                imgCapturedimage.setImageResource(R.mipmap.sign_up_project);
            }
        }
    }


}
