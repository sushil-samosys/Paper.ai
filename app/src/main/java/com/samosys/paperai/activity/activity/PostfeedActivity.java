package com.samosys.paperai.activity.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.samosys.paperai.R;
import com.samosys.paperai.activity.AudioVisualizer.LineBarVisualizer;
import com.samosys.paperai.activity.Bean.ParentBean;
import com.samosys.paperai.activity.Bean.ProjctBean;
import com.samosys.paperai.activity.Bean.WorkSpaceMemberBean;
import com.samosys.paperai.activity.FullVideoview.FullscreenVideoLayout;
import com.samosys.paperai.activity.utils.AppConstants;
import com.samosys.paperai.activity.utils.NetworkAvailablity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

import static org.apache.commons.io.IOUtils.toByteArray;


//import com.alibaba.fastjson.JSON;

public class PostfeedActivity extends AppCompatActivity {


    public static final String[] WRITE_EXTERNAL_STORAGE_PERMS = {
            Manifest.permission.RECORD_AUDIO
    };
    public static final int AUDIO_PERMISSION_REQUEST_CODE = 102;

    protected MediaPlayer mediaPlayer1;
    ImageButton reply;
    //https://github.com/lgvalle/Material-Animations
    ParseFile parseFile = null;
    EditText caption_txt;
    TextView btn_sendPost;
    RelativeLayout include_visual;

    ImageView btnVolume, imgSingleUser;
    LineBarVisualizer lineBarVisualizer;
    FullscreenVideoLayout myVideoView;
    MediaPlayer mediaPlayer;
    Spinner projectSpinner;
    RelativeLayout rlEditext, rlSingleUser, rlpublic, rlcard, rlmember;
    Boolean editIcon = true;
    ImageButton ib_play_pause;
    ImageView imgPrivacy, imgpublic, imgcard, imgBinoculor;
    LinearLayout ll_privecy;
    boolean Privacy = false;
    private AudioManager mAudioManager;
    private int position = 0;
    private String strFile = "", post_type = "", workid = "", projectID = "", imageURL = "", privacy = "3";
    private RelativeLayout RL_imagefeed, rl_vidio;
    private AQuery aq;
    private ImageView imgCapturedimage, btn_fullscren, ImgPostImage;
    private TextView work_name, txt_text, txtduration;
    private boolean isHideToolbarView = false;
    private ACProgressFlower dialog;
    private MediaController mediaControls;
    private ArrayList<WorkSpaceMemberBean> UserList;
    private ArrayList<ProjctBean> projectList;

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
        imageURL = getIntent().getStringExtra("imageURL");
        rlmember = (RelativeLayout) findViewById(R.id.rlmember);
        rlSingleUser = (RelativeLayout) findViewById(R.id.rlSingleUser);
        rlcard = (RelativeLayout) findViewById(R.id.rlcard);
        rlpublic = (RelativeLayout) findViewById(R.id.rlpublic);
        Log.e("strFile", strFile + ">>>" + post_type);
        projectSpinner = (Spinner) findViewById(R.id.projectSpinner);
        mediaPlayer1 = new MediaPlayer();
        UserList = new ArrayList<>();
        ll_privecy = (LinearLayout) findViewById(R.id.ll_privecy);
        btnVolume = (ImageView) findViewById(R.id.btnVolume);
        imgSingleUser = (ImageView) findViewById(R.id.imgSingleUser);
        rlEditext = (RelativeLayout) findViewById(R.id.rlEditext);
        workid = AppConstants.loadPreferences(PostfeedActivity.this, "workid");
        projectID = AppConstants.loadPreferences(PostfeedActivity.this, "projectID");
        include_visual = (RelativeLayout) findViewById(R.id.include_visual);
        lineBarVisualizer = (LineBarVisualizer) findViewById(R.id.visualizer);
        imgpublic = (ImageView) findViewById(R.id.imgpublic);
        imgcard = (ImageView) findViewById(R.id.imgcard);
        imgPrivacy = (ImageView) findViewById(R.id.imgPrivacy);
        imgBinoculor = (ImageView) findViewById(R.id.imgBinoculor);
        caption_txt = (EditText) findViewById(R.id.caption_txt);
        projectList = new ArrayList<>();
        myVideoView = (FullscreenVideoLayout) findViewById(R.id.video_view);
        work_name = (TextView) findViewById(R.id.workname);
        txtduration = (TextView) findViewById(R.id.txtduration);
        btn_sendPost = (TextView) findViewById(R.id.btn_sendPost);
        imgCapturedimage = (ImageView) findViewById(R.id.imagesPost);
        ImgPostImage = (ImageView) findViewById(R.id.ImgPostImage);
        btn_fullscren = (ImageView) findViewById(R.id.btn_fullscren);
        rl_vidio = (RelativeLayout) findViewById(R.id.rl_vidio);

        ib_play_pause = (ImageButton) findViewById(R.id.ib_play_pause);
        RL_imagefeed = (RelativeLayout) findViewById(R.id.RL_imagefeed);
        // customMultiAutoComplete.onViewAttached(caption_txt);
        myVideoView.setShouldAutoplay(false);


        mAudioManager = (AudioManager) PostfeedActivity.this.getSystemService(Context.AUDIO_SERVICE);

        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }
        if (mediaControls == null) {

            mediaControls = new MediaController(PostfeedActivity.this);

        }
        imgPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Privacy) {
                    imgPrivacy.setImageDrawable(getResources().getDrawable(R.mipmap.forgot_password_icon1));
                    ll_privecy.setVisibility(View.VISIBLE);
                    Privacy = true;
                } else {
                    Privacy = false;
                    imgPrivacy.setImageDrawable(getResources().getDrawable(R.mipmap.home_lock));
                    ll_privecy.setVisibility(View.GONE);
                }

            }
        });

        rlSingleUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlSingleUser.setBackgroundColor(getResources().getColor(R.color.txt_dark));
                rlpublic.setBackgroundColor(getResources().getColor(R.color.white));
                rlcard.setBackgroundColor(getResources().getColor(R.color.white));
                rlmember.setBackgroundColor(getResources().getColor(R.color.white));
                imgSingleUser.setImageDrawable(getResources().getDrawable(R.mipmap.home_user_white));
                imgpublic.setImageDrawable(getResources().getDrawable(R.mipmap.home_double_user));
                imgcard.setImageDrawable(getResources().getDrawable(R.mipmap.home_id_card));
                imgBinoculor.setImageDrawable(getResources().getDrawable(R.mipmap.home_binocular));
                privacy = "0";
            }
        });
        rlmember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlmember.setBackgroundColor(getResources().getColor(R.color.txt_dark));
                rlSingleUser.setBackgroundColor(getResources().getColor(R.color.white));
                rlpublic.setBackgroundColor(getResources().getColor(R.color.white));
                rlcard.setBackgroundColor(getResources().getColor(R.color.white));

                imgSingleUser.setImageDrawable(getResources().getDrawable(R.mipmap.home_user));
                imgpublic.setImageDrawable(getResources().getDrawable(R.mipmap.home_double_user));
                imgcard.setImageDrawable(getResources().getDrawable(R.mipmap.home_id_card));
                imgBinoculor.setImageDrawable(getResources().getDrawable(R.mipmap.home_binocular_white));
                privacy = "3";
            }
        });

        rlpublic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlpublic.setBackgroundColor(getResources().getColor(R.color.txt_dark));
                rlSingleUser.setBackgroundColor(getResources().getColor(R.color.white));
                rlcard.setBackgroundColor(getResources().getColor(R.color.white));
                rlmember.setBackgroundColor(getResources().getColor(R.color.white));
                imgSingleUser.setImageDrawable(getResources().getDrawable(R.mipmap.home_user));
                imgpublic.setImageDrawable(getResources().getDrawable(R.mipmap.home_double_user_white));
                imgcard.setImageDrawable(getResources().getDrawable(R.mipmap.home_id_card));
                imgBinoculor.setImageDrawable(getResources().getDrawable(R.mipmap.home_binocular));
                privacy = "1";
            }
        });

        rlcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlpublic.setBackgroundColor(getResources().getColor(R.color.white));
                rlcard.setBackgroundColor(getResources().getColor(R.color.txt_dark));
                rlSingleUser.setBackgroundColor(getResources().getColor(R.color.white));
                rlmember.setBackgroundColor(getResources().getColor(R.color.white));
                privacy = "2";
                imgSingleUser.setImageDrawable(getResources().getDrawable(R.mipmap.home_user));
                imgpublic.setImageDrawable(getResources().getDrawable(R.mipmap.home_double_user));
                imgcard.setImageDrawable(getResources().getDrawable(R.mipmap.home_id_card_white));
                imgBinoculor.setImageDrawable(getResources().getDrawable(R.mipmap.home_binocular));

            }
        });
        if (NetworkAvailablity.chkStatus(PostfeedActivity.this)) {
            getprojectlist();
            getprojectmember();

        } else {
            Toast.makeText(PostfeedActivity.this, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
        }


        reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null) {
                    mediaPlayer.seekTo(0);
                }

            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

                ib_play_pause.setImageDrawable(ContextCompat.getDrawable(
                        PostfeedActivity.this,
                        R.drawable.ic_play_red_48dp));
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
        });
        ib_play_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initialize();
                if (mediaPlayer != null) {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                        ib_play_pause.setImageDrawable(ContextCompat.getDrawable(
                                PostfeedActivity.this,
                                R.drawable.ic_play_red_48dp));
                    } else {
                        mediaPlayer.start();
                        ib_play_pause.setImageDrawable(ContextCompat.getDrawable(
                                PostfeedActivity.this,
                                R.drawable.ic_pause_red_48dp));
                    }
                } else {
                    mediaPlayer = new MediaPlayer();

                    lineBarVisualizer.setColor(ContextCompat.getColor(PostfeedActivity.this, R.color.colorPrimary));

                    // define custom number of bars you want in the visualizer between (10 - 256).
                    lineBarVisualizer.setDensity(90f);

                    // Set your media player to the visualizer.
                    lineBarVisualizer.setPlayer(mediaPlayer.getAudioSessionId());
//                    mediaPlayer.seekTo(0);

                    try {
                        mediaPlayer.setDataSource(strFile);
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                        mediaPlayer.setLooping(false);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
//            mediaPlayer.start();

//                    mediaPlayer.start();
                    ib_play_pause.setImageDrawable(ContextCompat.getDrawable(
                            PostfeedActivity.this,
                            R.drawable.ic_pause_red_48dp));

                }

            }
        });

        projectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                ((TextView) adapterView.getChildAt(0)).setTextColor(getResources().getColor(R.color.txt_dark));
                ((TextView) adapterView.getChildAt(0)).setTextSize(13);

                ((TextView) adapterView.getChildAt(0)).setSingleLine();
                //if (userIsInteracting) {
                projectID = projectList.get(i).getObjectId();


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        txt_text = (TextView) findViewById(R.id.txt_text);

        work_name.setText(AppConstants.loadPreferences(PostfeedActivity.this, "workname"));

        Log.e("strFile", strFile);
        // post_type="2";


        if (post_type.equals("0")) {
            btn_fullscren.setVisibility(View.GONE);
            RL_imagefeed.setVisibility(View.GONE);

        } else if (post_type.equals("2")) {

            RL_imagefeed.setVisibility(View.VISIBLE);
            File file = new File(strFile);
//          int a=  getAudioInfo(file);
//            txtduration.setText(""+a);
            converttexttospeech(file, strFile, post_type);


        } else if (post_type.equals("1")) {
            RL_imagefeed.setVisibility(View.VISIBLE);
            imgCapturedimage.setVisibility(View.VISIBLE);

            include_visual.setVisibility(View.GONE);
            rl_vidio.setVisibility(View.GONE);
            if (!strFile.equals("")) {
                setImage(strFile);

            } else {
                RL_imagefeed.setVisibility(View.GONE);
            }
        } else if (post_type.equals("3")) {
            RL_imagefeed.setVisibility(View.VISIBLE);

            rl_vidio.setVisibility(View.VISIBLE);
            imgCapturedimage.setVisibility(View.GONE);
            include_visual.setVisibility(View.GONE);

            myVideoView.setActivity(this);

            Uri videoUri = Uri.parse(strFile);
            try {
                myVideoView.setVideoURI(videoUri);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (post_type.equals("4")) {
            RL_imagefeed.setVisibility(View.VISIBLE);
            imgCapturedimage.setVisibility(View.VISIBLE);
            include_visual.setVisibility(View.VISIBLE);
            rl_vidio.setVisibility(View.GONE);
            // callimagewidAudio();
            if (!strFile.equals("")) {
                setImage(imageURL);

            } else {
                RL_imagefeed.setVisibility(View.GONE);
            }
            File file = new File(strFile);
//          int a=  getAudioInfo(file);
//            txtduration.setText(""+a);
            converttexttospeech(file, strFile, post_type);
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

                        ParseFile audioFile = new ParseFile("file.m4a", soundBytes);
                        createPostImage(audioFile);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (post_type.equals("3")) {
                    byte[] soundBytes;
                    try {
                        InputStream inputStream =
                                getContentResolver().openInputStream(Uri.fromFile(new File(strFile)));
                        //  soundBytes = new byte[inputStream.available()];
                        soundBytes = toByteArray(inputStream);

                        ParseFile audioFile = new ParseFile("video.mp4", soundBytes);
                        createPostImage(audioFile);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }
        });


        btn_fullscren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                rlEditext.addView(RL_imagefeed);
//                rlEditext.bringToFront();
//                rlEditext.invalidate();


                if (editIcon) {
                    btn_fullscren.setImageDrawable(getResources().getDrawable(R.mipmap.compress_icon));

                    RL_imagefeed.animate()
                            .translationY(-700)


                            .alpha(1.0f)

                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);


                                }
                            });
                    editIcon = false;
                    RL_imagefeed.setVisibility(View.GONE);
                } else {
                    btn_fullscren.setImageDrawable(getResources().getDrawable(R.mipmap.expand_icon));
                    editIcon = true;

                    RL_imagefeed.animate()
                            .translationY(0)

                            .alpha(1.0f)

                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);


                                }
                            });
                    RL_imagefeed.setVisibility(View.VISIBLE);
                }


            }
        });


    }

    private void callimagewidAudio() {

        if (!strFile.equals("")) {
            setImage_widaudio(strFile);

        } else {
            RL_imagefeed.setVisibility(View.GONE);
        }

        File file = new File(strFile);
//          int a=  getAudioInfo(file);
//            txtduration.setText(""+a);


    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        myVideoView.resize();
    }

    private void setImage_widaudio(String strFile) {

        File newFile = new File(strFile);

        File file = new File(newFile.getAbsolutePath());
        if (file.exists()) {
            BitmapAjaxCallback cb = new BitmapAjaxCallback();
            cb.targetWidth(500).rotate(true);
            aq.id(ImgPostImage).image(newFile, false, 500, cb);
            //aq.id(imgCapturedimage).image(new File(listitem.get(position).getPost_image()), false, 500, cb);
        } else {

            if (newFile.exists()) {
                aq.ajax(newFile.getAbsolutePath(), File.class, new AjaxCallback<File>() {
                    @Override
                    public void callback(String url, File bm, AjaxStatus status) {

                        if (bm != null) {
                            BitmapAjaxCallback cb = new BitmapAjaxCallback();
                            cb.targetWidth(500).rotate(true);
                            aq.id(ImgPostImage).image(bm, true, 500, cb);
                        } else {
                            ImgPostImage.setImageResource(R.mipmap.sign_up_project);
                        }
                    }
                });
            } else {
                ImgPostImage.setImageResource(R.mipmap.sign_up_project);
            }
        }
    }


    private void converttexttospeech(final File file, final String strFile, final String woImage) {
        final ACProgressFlower acProgressFlower = new ACProgressFlower.Builder(PostfeedActivity.this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)

                .fadeColor(Color.DKGRAY).build();
        acProgressFlower.setTitle("Converting speech to text");
//        final ACProgressFlower acProgressFlower = AppConstants.CustomshowProgressDialog(PostfeedActivity.this, "Converting speech to text");
        acProgressFlower.show();
        String url = "https://paprspeechtotext.azurewebsites.net/api/Recognize?code=esVh5fZUJUqnm6N4JeW7FofYCmNL9ltW7fxpfOSIzq3m0VrlaRI3YA==";


        AjaxCallback<JSONObject> cb = new AjaxCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject html, AjaxStatus status) {


                Log.e("APIRESPONSE", ">>>>>" + url);
                Log.e("APIRESPONSE", ">>>>>" + html);
                Log.e("APIRESPONSE", ">>>>>" + status);
                if (html != null) {
                    try {
                        String Status = html.getString("Status");
                        if (Status.equals("Success")) {

                            if (acProgressFlower.isShowing()) {
                                acProgressFlower.dismiss();
                            }
                            String SpeechtoText = html.getString("SpeechtoText");
                            caption_txt.setText(SpeechtoText);
                            lineBarVisualizer.setColor(ContextCompat.getColor(PostfeedActivity.this, R.color.colorPrimary));

                            // define custom number of bars you want in the visualizer between (10 - 256).
                            lineBarVisualizer.setDensity(90f);

                            // Set your media player to the visualizer.
                            lineBarVisualizer.setPlayer(mediaPlayer.getAudioSessionId());
                            if (woImage.equals("2")) {
                                rl_vidio.setVisibility(View.GONE);
                                imgCapturedimage.setVisibility(View.GONE);
                                include_visual.setVisibility(View.VISIBLE);


                            } else {
                                rl_vidio.setVisibility(View.GONE);
                                imgCapturedimage.setVisibility(View.VISIBLE);
                                include_visual.setVisibility(View.VISIBLE);


                            }
                        } else {
                            if (acProgressFlower.isShowing()) {
                                acProgressFlower.dismiss();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        if (acProgressFlower.isShowing()) {
                            acProgressFlower.dismiss();
                        }
                    }
                } else {
                    Toast.makeText(PostfeedActivity.this, "Please Try Again", Toast.LENGTH_SHORT).show();
                    if (acProgressFlower.isShowing()) {
                        acProgressFlower.dismiss();
                    }
                }


            }


        };
//
        cb.header("token", "b79f0858-f00f-4b8e-9341-75b37cc85a61");
//
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("AudioFile", file);
        params.put("AudioType", "audio/m4a");
        params.put("Model_ID", "43442cfe-6d3f-4a65-a268-e0eff02ebc53");

        cb.params(params);


        aq.ajax(url, JSONObject.class, cb);
    }


    private void getprojectlist() {

        projectList.clear();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Project");

        query.whereEqualTo("workspace", ParseObject.createWithoutData("WorkSpace", workid));
        query.include("user");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    try {
                        //  childBeans = new ArrayList<>();
                        // parentBeans.clear();
                        Log.e("ProjectDATA", "d>>>>>" + objects.size());
                        for (int i = 0; i < objects.size(); i++) {

                            ParseObject parseObject = objects.get(i).getParseObject("user");

                            String objectId1 = parseObject.getObjectId();
                            Log.e("objectId1", "d>>>>>" + objectId1);

                            String strdefault = "";
                            String objectId = objects.get(i).getObjectId();
                            String name = objects.get(i).getString("name");
                            String updatedAt = objects.get(i).getString("updatedAt");
                            String workspaceID = objects.get(i).getString("workspaceID");
                            String objective = objects.get(i).getString("objective");
                            String createdAt = objects.get(i).getString("createdAt");
                            ParseFile ws_image = (ParseFile) objects.get(i).get("image");
                            String image = ws_image.getUrl();
                            String archive = objects.get(i).getString("archive");
                            String type = objects.get(i).getString("type");
                            if (objects.get(i).has("default")) {
                                strdefault = objects.get(i).getString("default");
                            }

                            Log.e("Project_IDDSs", name + ">>>>>>>>>" + archive);

//
                            Log.e("Project_IDDSs", name + ">>>>>>>>>" + archive);

                            if (archive.equals("0")) {
                                if (objectId1.equals(ParseUser.getCurrentUser().getObjectId())) {


                                    projectList.add(new ProjctBean(objectId, name, updatedAt, workspaceID, objective, createdAt, image, type, objects.get(i), archive));


                                 } else {
                                    if (type.equals("1")) {
                                        projectList.add(new ProjctBean(objectId, name, updatedAt, workspaceID, objective, createdAt, image, type, objects.get(i), archive));


                                     }
                                }
                            }








//                            }

                        }
                        projectSpinner.setAdapter(new MyAdapter(PostfeedActivity.this, android.R.layout.simple_dropdown_item_1line, projectList));

                    } catch (NullPointerException ea) {
                        ea.printStackTrace();
                    }
                } else {

                    // error
                }
//

            }
        });

    }

    private void getprojectmember() {


        UserList.clear();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("workspace_follower");
        query.whereEqualTo("workspace", ParseObject.createWithoutData("WorkSpace", workid));
        Log.e("getmyfolloelist", workid + "");
        query.include("user");


        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {

                if (e == null) {
                    Log.e("getmyfolloelist", objects.size() + "");
                    if (objects.size() > 0) {


                        for (int i = 0; i < objects.size(); i++) {
                            // String objectId = objects.get(i).getObjectId();
//


//                            ParseObject user = ParseObject.createWithoutData("WorkSpace", "user");
                            ParseObject parseObject = objects.get(i).getParseObject("user");

                            String objectId = parseObject.getObjectId();
                            String username = parseObject.getString("username");
                            String email = parseObject.getString("email");


                            String updatedAt = parseObject.getUpdatedAt().toString();
                            String createdAt = parseObject.getCreatedAt().toString();
                            String passion = parseObject.getString("passion");
                            ParseFile profileimage = (ParseFile) parseObject.get("profileimage");
                            String img = profileimage.getUrl();
                            String title = parseObject.getString("title");
                            String fullname = parseObject.getString("fullname");
                            String campanyrole = parseObject.getString("campanyrole");
                            String communityrole = parseObject.getString("communityrole");
                            String address = parseObject.getString("address");

                            Log.e("EMAIL_USER", objectId + ">>" + fullname);
                            UserList.add(new WorkSpaceMemberBean(objectId, username, email, updatedAt, createdAt, passion, img, title, fullname
                                    , campanyrole, communityrole, address));
                        }


//                        AutoCompleteTypeAdapter<WorkSpaceMemberBean> nameTypeAdapter =
//                                AutoCompleteTypeAdapter.Build.from(new CountryViewBinder(), new CountryNameTokenFilter());
//                        // listener for tokens added/removed by the user (see onTokenAdded() and onTokenRemoved() below)
//                        nameTypeAdapter.setOnTokensChangedListener(this);

                        // type adapter to match country codes prefixed with '@'
                        // note: the data type here could be different (i.e. a Region object)
//                        AutoCompleteTypeAdapter<WorkSpaceMemberBean> codeTypeAdapter =
//                                AutoCompleteTypeAdapter.Build.from(new CountryViewBinder(), new CountryCodeTokenFilter());
//
//                        // setting items synchronously since we already have the list
//                        // this could be also done later on in the likely case the data set comes from network or disk
//                       // nameTypeAdapter.setItems(countryList);
//                        codeTypeAdapter.setItems(UserList);

                        // build the custom MultiAutoComplete by passing the required Tokenizer and type adapters
//                        customMultiAutoComplete = new MultiAutoComplete.Builder()
//                                .tokenizer(new PrefixTokenizer('@', '#'))
//                               // .addTypeAdapter(nameTypeAdapter)
//                                .addTypeAdapter(codeTypeAdapter)
//                                .delayer(new TestDelayer())
//                                .build();
                    }

                } else {
                    Toast.makeText(PostfeedActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    Log.e("else", e.getMessage());
                    // error
                }

            }
        });
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

    //    public void playPause(View view) {
//        playPauseBtnClicked((ImageButton) view);
//    }
//
//    public void playPauseBtnClicked(ImageButton btnPlayPause) {
//        if (mediaPlayer != null) {
//            if (mediaPlayer.isPlaying()) {
//                mediaPlayer.pause();
//                btnPlayPause.setImageDrawable(ContextCompat.getDrawable(
//                        this,
//                        R.drawable.ic_play_red_48dp));
//            } else {
//                mediaPlayer.start();
//                btnPlayPause.setImageDrawable(ContextCompat.getDrawable(
//                        this,
//                        R.drawable.ic_pause_red_48dp));
//            }
//        }
//    }
    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        switch (requestCode) {
            case AUDIO_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setPlayer();
                } else {
                    this.finish();
                }
        }
    }

    private void createPostImage(ParseFile parseFile) {


        ParseObject gameScore = new ParseObject("Post");
        gameScore.put("user", ParseObject.createWithoutData("_User", ParseUser.getCurrentUser().getObjectId()));
//
        gameScore.put("text", caption_txt.getText().toString());
        gameScore.put("workspace", ParseObject.createWithoutData("WorkSpace", workid));
        gameScore.put("project", ParseObject.createWithoutData("Project", projectID));
        gameScore.put("post_type", post_type);
        gameScore.put("CommentCount", 0);
        gameScore.put("likesCount", 0);
        gameScore.put("privacy", privacy);
        gameScore.put("post_type", post_type);
        if (post_type.equals("1")) {
            if (parseFile != null) {
                gameScore.put("postImage", parseFile);
            }
        } else if (post_type.equals("2")) {
            gameScore.put("post_file", parseFile);
            gameScore.put("media_duration", HomeFeedActivity.prog + " sec");
        } else if (post_type.equals("3")) {
            gameScore.put("post_file", parseFile);
            // gameScore.put("media_duration", HomeFeedActivity.prog + " sec");
        } else {

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


    private class MyAdapter extends ArrayAdapter<ProjctBean> {
        Context context;

        List<ProjctBean> patientlist;

        public MyAdapter(Activity activity, int detail_color_layout, List<ProjctBean> patientlist) {
            super(activity, detail_color_layout, patientlist);
            this.context = activity;
            this.patientlist = patientlist;


        }


        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            // TODO Auto-generated method stub
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View v, ViewGroup parent) {
            // TODO Auto-generated method stub

            View row = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.country_item, parent, false);

            TextView make = (TextView) row.findViewById(R.id.color);
            make.setText(patientlist.get(position).getName());

            return row;

        }
    }


}

