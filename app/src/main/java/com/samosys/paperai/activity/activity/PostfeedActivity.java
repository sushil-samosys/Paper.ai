package com.samosys.paperai.activity.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.samosys.paperai.activity.AudioVisualizer.LineBarVisualizer;
import com.samosys.paperai.activity.utils.AppConstants;
import com.samosys.paperai.activity.utils.First_Char_Capital;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

import static org.apache.commons.io.IOUtils.toByteArray;

//import com.alibaba.fastjson.JSON;

public class PostfeedActivity extends AppCompatActivity {


    public static final String[] WRITE_EXTERNAL_STORAGE_PERMS = {
            Manifest.permission.RECORD_AUDIO
    };
    public static final int AUDIO_PERMISSION_REQUEST_CODE = 102;
    protected MediaPlayer mediaPlayer;
    ImageButton reply;
    //https://github.com/lgvalle/Material-Animations
    ParseFile parseFile = null;
    EditText caption_txt;
    TextView btn_sendPost;
    int linHeight;
    int startX, startY;
    int deltaX, deltaY;
    int animationDuration = 400;
    View myView;
    RelativeLayout include_visual;
    boolean isUp = true;
    LineBarVisualizer lineBarVisualizer;
    private String strFile = "", post_type = "";
    private RelativeLayout RL_imagefeed;
    private AQuery aq;
    private ImageView imgCapturedimage, btn_fullscren;
    private TextView work_name, txt_text;
    private boolean isHideToolbarView = false;
    private ACProgressFlower dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppConstants.getstatusbar(PostfeedActivity.this);
        AppConstants.hideKeyboard(PostfeedActivity.this);
        setContentView(R.layout.activity_postfeed);
        aq = new AQuery(PostfeedActivity.this);
        reply = (ImageButton) findViewById(R.id.reply);
        strFile = getIntent().getStringExtra("file");
        post_type = getIntent().getStringExtra("post_type");
//        post_type="2"
        Log.e("onCreate", strFile + ">>>>>>>>" + post_type);
        include_visual = (RelativeLayout) findViewById(R.id.include_visual);
        lineBarVisualizer = (LineBarVisualizer) findViewById(R.id.visualizer);
        caption_txt = (EditText) findViewById(R.id.caption_txt);
        work_name = (TextView) findViewById(R.id.workname);
        btn_sendPost = (TextView) findViewById(R.id.btn_sendPost);
        imgCapturedimage = (ImageView) findViewById(R.id.imagesPost);
        btn_fullscren = (ImageView) findViewById(R.id.btn_fullscren);
        RL_imagefeed = (RelativeLayout) findViewById(R.id.RL_imagefeed);

        initialize();
        lineBarVisualizer.setDensity(50);

        // Set your media player to the visualizer.
        lineBarVisualizer.setPlayer(mediaPlayer.getAudioSessionId());
        lineBarVisualizer.setColor(ContextCompat.getColor(this, R.color.white));
        txt_text = (TextView) findViewById(R.id.txt_text);

        work_name.setText(First_Char_Capital.capitalizeString(AppConstants.loadPreferences(PostfeedActivity.this, "workname")));

        Log.e("strFile", strFile);
        // post_type="2";

        if (strFile.equals("")) {
            RL_imagefeed.setVisibility(View.GONE);
        }

        if (post_type.equals("2")) {

            imgCapturedimage.setVisibility(View.GONE);
            include_visual.setVisibility(View.VISIBLE);
        } else if (post_type.equals("1")) {

            imgCapturedimage.setVisibility(View.VISIBLE);
            include_visual.setVisibility(View.GONE);
            if (!strFile.equals("")) {
                setImage(strFile);

            } else {
                RL_imagefeed.setVisibility(View.GONE);
            }
        }


        btn_sendPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new ACProgressFlower.Builder(PostfeedActivity.this)
                        .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                        .themeColor(Color.WHITE)

                        .fadeColor(Color.DKGRAY).build();
                dialog.show();

                if (post_type.equals("1")) {
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
                } else if (post_type.equals("0")) {
                    if (caption_txt.getText().toString().equals("") || caption_txt.getText().toString() == null) {
                        Toast.makeText(PostfeedActivity.this, "Please Enter something...", Toast.LENGTH_SHORT).show();
                    } else {
                        createPostImage(parseFile);
                    }

                } else if (post_type.equals("2")) {
                    byte[] soundBytes;
                    try {
                        InputStream inputStream =
                                getContentResolver().openInputStream(Uri.fromFile(new File(strFile)));
                        //  soundBytes = new byte[inputStream.available()];
                        soundBytes = toByteArray(inputStream);

                        ParseFile audioFile = new ParseFile("file.m4v", soundBytes);
                        createPostImage(audioFile);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }
        });


//        btn_fullscren.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                //onSlideViewButtonClick(v);
//
//            }
//        });


    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

    private void initialize() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(WRITE_EXTERNAL_STORAGE_PERMS, AUDIO_PERMISSION_REQUEST_CODE);
        } else {
            setPlayer();
        }
    }


    private void setPlayer() {

        //mediaPlayer = MediaPlayer.create(this, R.raw.red_e);
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(strFile);
            mediaPlayer.prepare();
//            mediaPlayer.start();
            mediaPlayer.setLooping(false);
        } catch (IOException e) {
            e.printStackTrace();
        }


//        init();
    }


    public void playPause(View view) {
        playPauseBtnClicked((ImageButton) view);
    }

    public void playPauseBtnClicked(ImageButton btnPlayPause) {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                btnPlayPause.setImageDrawable(ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_play_red_48dp));
            } else {
                mediaPlayer.start();
                btnPlayPause.setImageDrawable(ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_pause_red_48dp));
            }
        }
    }











    private void createPostImage(ParseFile parseFile) {


        ParseObject gameScore = new ParseObject("Post");
        gameScore.put("user", ParseObject.createWithoutData("_User", ParseUser.getCurrentUser().getObjectId()));
//
        gameScore.put("text", caption_txt.getText().toString());
        gameScore.put("workspace", ParseObject.createWithoutData("WorkSpace", AppConstants.loadPreferences(PostfeedActivity.this, "workid")));
        gameScore.put("project", ParseObject.createWithoutData("Project", AppConstants.loadPreferences(PostfeedActivity.this, "projectID")));
        gameScore.put("post_type", post_type);
        if (post_type.equals("1")) {
            if (parseFile != null) {
                gameScore.put("postImage", parseFile);
            }
        } else if (post_type.equals("2")) {
            gameScore.put("post_file", parseFile);
            gameScore.put("media_duration", HomeFeedActivity.prog + " sec");
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
                    Log.e("POSTING", e.getMessage());
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
//    public class ReaderTask extends AsyncTask<Void, Void, WaveFormInfo> {
//
//        @Override protected WaveFormInfo doInBackground(Void... params) {
//            InputStream inputStream = null;
//            try {
//                inputStream = getResources().openRawResource(R.raw.waveform);
//                byte[] data = new byte[inputStream.available()];
//                inputStream.read(data);
//                Toast.makeText(PostfeedActivity.this, strFile, Toast.LENGTH_SHORT).show();
//                return JSON.parseObject(data, WaveFormInfo.class);
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            } finally {
//                try {
//                    if (inputStream != null) {
//                        inputStream.close();
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            return null;
//        }
//    }

}
