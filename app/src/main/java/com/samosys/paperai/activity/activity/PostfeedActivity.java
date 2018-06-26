package com.samosys.paperai.activity.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

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
import com.percolate.mentions.Mentions;
import com.percolate.mentions.QueryListener;
import com.percolate.mentions.SuggestionsListener;
import com.samosys.paperai.R;
import com.samosys.paperai.activity.AudioVisualizer.LineBarVisualizer;
import com.samosys.paperai.activity.Bean.WorkSpaceMemberBean;
import com.samosys.paperai.activity.adapter.MentionAutoCompleteAdapter;
import com.samosys.paperai.activity.utils.AppConstants;
import com.samosys.paperai.activity.utils.First_Char_Capital;
import com.samosys.paperai.activity.utils.MentionsLoaderUtils;
import com.samosys.paperai.activity.utils.NetworkAvailablity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

import static org.apache.commons.io.IOUtils.toByteArray;

//import com.alibaba.fastjson.JSON;

public class PostfeedActivity extends AppCompatActivity implements QueryListener, SuggestionsListener {


    public static final String[] WRITE_EXTERNAL_STORAGE_PERMS = {
            Manifest.permission.RECORD_AUDIO
    };
    public static final int AUDIO_PERMISSION_REQUEST_CODE = 102;
    private static final String BUCKET = "cities-memory";
    protected MediaPlayer mediaPlayer1;
    ImageButton reply;
    //https://github.com/lgvalle/Material-Animations
    ParseFile parseFile = null;
    EditText caption_txt;
    TextView btn_sendPost;
    RelativeLayout include_visual;
    boolean VOLUME = false;
    ImageView btnVolume;
    LineBarVisualizer lineBarVisualizer;
    VideoView myVideoView;
    MediaPlayer mediaPlayer;
    Mentions mentions;
    MentionsLoaderUtils mentionsLoaderUtils;
    private int position = 0;
    private String strFile = "", post_type = "", workid = "";
    private RelativeLayout RL_imagefeed, rl_vidio;
    private AQuery aq;
    private ImageView imgCapturedimage, btn_fullscren;
    private TextView work_name, txt_text;
    private boolean isHideToolbarView = false;
    private ACProgressFlower dialog;
    private MediaController mediaControls;
    private Button btnPlay, btnStop, btnMute, btnUnMute;
    private ArrayList<WorkSpaceMemberBean> UserList;
    MentionAutoCompleteAdapter mentionAutoCompleteAdapter;
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
//        post_type="2".
        mediaPlayer1 = new MediaPlayer();
        UserList = new ArrayList<>();
        btnVolume = (ImageView) findViewById(R.id.btnVolume);

        workid = AppConstants.loadPreferences(PostfeedActivity.this, "workid");
        include_visual = (RelativeLayout) findViewById(R.id.include_visual);
        lineBarVisualizer = (LineBarVisualizer) findViewById(R.id.visualizer);
        caption_txt = (EditText) findViewById(R.id.caption_txt);

//        mentions = new Mentions.Builder(this, caption_txt)
//                .suggestionsListener(this)
//                .queryListener(this)
//
//                .build();
//        mentionsLoaderUtils = new MentionsLoaderUtils(this);
//        workSpaceMemberBean = new WorkSpaceMemberBean();
        work_name = (TextView) findViewById(R.id.workname);
        btn_sendPost = (TextView) findViewById(R.id.btn_sendPost);
        imgCapturedimage = (ImageView) findViewById(R.id.imagesPost);
        btn_fullscren = (ImageView) findViewById(R.id.btn_fullscren);
        rl_vidio = (RelativeLayout) findViewById(R.id.rl_vidio);
        RL_imagefeed = (RelativeLayout) findViewById(R.id.RL_imagefeed);


        myVideoView = (VideoView) findViewById(R.id.video_view);

        if (mediaControls == null) {

            mediaControls = new MediaController(PostfeedActivity.this);

        }
        if (NetworkAvailablity.chkStatus(PostfeedActivity.this)) {

            getprojectmember();

        } else {
            Toast.makeText(PostfeedActivity.this, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
        }
//        btnVolume.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!VOLUME){
//                    mediaPlayer1.setVolume(0,0);
//
//                }else {
//                    mediaPlayer1.setVolume(10,10);
//                }
//
//            }
//        });


        txt_text = (TextView) findViewById(R.id.txt_text);

        work_name.setText(First_Char_Capital.capitalizeString(AppConstants.loadPreferences(PostfeedActivity.this, "workname")));

        Log.e("strFile", strFile);
        // post_type="2";

        if (strFile.equals("")) {
            RL_imagefeed.setVisibility(View.GONE);
            rl_vidio.setVisibility(View.GONE);
        }

        if (post_type.equals("2")) {


            rl_vidio.setVisibility(View.GONE);
            imgCapturedimage.setVisibility(View.GONE);
            include_visual.setVisibility(View.VISIBLE);

        } else if (post_type.equals("1")) {

            imgCapturedimage.setVisibility(View.VISIBLE);
            include_visual.setVisibility(View.GONE);
            rl_vidio.setVisibility(View.GONE);
            if (!strFile.equals("")) {
                setImage(strFile);

            } else {
                RL_imagefeed.setVisibility(View.GONE);
            }
        } else if (post_type.equals("3")) {

            rl_vidio.setVisibility(View.VISIBLE);
            imgCapturedimage.setVisibility(View.GONE);
            include_visual.setVisibility(View.GONE);

            myVideoView.setMediaController(mediaControls);

            myVideoView.start();

            myVideoView.pause();

            myVideoView.resume();

            myVideoView.seekTo(position);

            initialize();
            lineBarVisualizer.setDensity(50);

            // Set your media player to the visualizer.
            lineBarVisualizer.setPlayer(mediaPlayer.getAudioSessionId());
            lineBarVisualizer.setColor(ContextCompat.getColor(this, R.color.basecolor));

            Display display = getWindowManager().getDefaultDisplay();
            int width = display.getWidth();
            int height = display.getHeight();
//            myVideoView.setLayoutParams(new FrameLayout.LayoutParams(width,height));
            myVideoView.setVideoURI(Uri.parse(strFile));

        }
        myVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            public void onPrepared(MediaPlayer mediaPlayer) {

                mediaPlayer1 = mediaPlayer;
//                Toast.makeText(PostfeedActivity.this, "xdvdv", Toast.LENGTH_SHORT).show();
                myVideoView.seekTo(position);

//
                if (position == 0) {

                    myVideoView.start();

                } else {

                    myVideoView.pause();
                }

            }

        });

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


//        btn_fullscren.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                //onSlideViewButtonClick(v);
//
//            }
//        });


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
        gameScore.put("CommentCount", 0);
        gameScore.put("likesCount", 0);
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

    @Override
    public void onQueryReceived(String query) {

    }

    @Override
    public void displaySuggestions(boolean display) {

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

