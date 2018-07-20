package com.samosys.paperai.activity.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
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
import com.samosys.paperai.activity.Bean.ProjctBean;
import com.samosys.paperai.activity.Bean.WorkSpaceMemberBean;
import com.samosys.paperai.activity.autocomplete.Autocomplete;
import com.samosys.paperai.activity.autocomplete.AutocompleteCallback;
import com.samosys.paperai.activity.autocomplete.AutocompletePolicy;
import com.samosys.paperai.activity.autocomplete.AutocompletePresenter;
import com.samosys.paperai.activity.autocomplete.CharPolicy;
import com.samosys.paperai.activity.autocomplete.ProjectPresenter;
import com.samosys.paperai.activity.autocomplete.UserPresenter;
import com.samosys.paperai.activity.utils.AppConstants;
import com.samosys.paperai.activity.utils.CustomFonts;
import com.samosys.paperai.activity.utils.NetworkAvailablity;
import com.samosys.paperai.activity.utils.SimpleTooltip;

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
import java.util.Timer;
import java.util.TimerTask;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import cn.jzvd.JZVideoPlayerStandard;

import static org.apache.commons.io.IOUtils.toByteArray;


public class PostfeedActivity extends AppCompatActivity {


    public static final String[] WRITE_EXTERNAL_STORAGE_PERMS = {
            Manifest.permission.RECORD_AUDIO
    };
    public static final int AUDIO_PERMISSION_REQUEST_CODE = 102;
    public static ArrayList<WorkSpaceMemberBean> UserList;
    public static ArrayList<ProjctBean> projectList;
    public int duration = 0, startDur = 0;
    boolean Privacy = false;
    private ImageButton reply;
    private JSONObject subObject;
    private ParseFile parseFile = null;
    private EditText caption_txt;
    private TextView btn_sendPost, post_header;
    private RelativeLayout include_visual;
    private ImageView btnVolume, imgSingleUser, img_arroe, imgRateuser, imgMention;
    private LineBarVisualizer lineBarVisualizer;
    private MediaPlayer mediaPlayer;
    private Spinner projectSpinner;
    private RelativeLayout rlSingleUser, rlpublic, rlcard, rlmember;
    private LinearLayout rlEditext;
    private Boolean editIcon = true;
    private ImageButton ib_play_pause;
    private ImageView imgPrivacy, imgpublic, imgcard, imgBinoculor;
    private LinearLayout ll_privecy, llPlaybtn, ll_bottm;
    private CustomFonts customFonts;
    private JZVideoPlayerStandard jzVideoPlayerStandard;
    private ArrayList<String> strMentionList;
    private RelativeLayout RLMain;
    private Autocomplete mentionsAutocomplete;
    private Timer timer;
    private String strFile = "", post_type = "", workid = "", projectID = "", imageURL = "", privacy = "3", strdur = "";
    private RelativeLayout RL_imagefeed, rl_vidio, linbar;
    private AQuery aq;
    private ImageView imgCapturedimage, btn_fullscren;
    private TextView txtduration;
    private ACProgressFlower dialog;
    private MediaController mediaControls;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postfeed);
        AppConstants.getstatusbar(PostfeedActivity.this);


        findview();
        initialize();


        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }
        if (mediaControls == null) {

            mediaControls = new MediaController(PostfeedActivity.this);

        }
        lineBarVisualizer.setColor(ContextCompat.getColor(PostfeedActivity.this, R.color.colorAccent));

        // define custom number of bars you want in the visualizer between (10 - 256).
        lineBarVisualizer.setDensity(90f);
        //   lineBarVisualizer.setBackgroundColor(getResources().getColor(R.color.ksw_md_solid_shadow));

        // Set your media player to the visualizer.
        lineBarVisualizer.setPlayer(mediaPlayer.getAudioSessionId());
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

//
        img_arroe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        rlSingleUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new SimpleTooltip.Builder(PostfeedActivity.this)
                        .anchorView(rlSingleUser)
                        .text("Only you")
                        .gravity(Gravity.TOP)

                        .arrowColor(getResources().getColor(R.color.colorAccent))
                        .backgroundColor(getResources().getColor(R.color.colorAccent))
                        .animated(false)
                        .build()
                        .show();
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
                new SimpleTooltip.Builder(PostfeedActivity.this)
                        .anchorView(rlmember)
                        .text("Followers")
                        .gravity(Gravity.TOP)
                        .arrowColor(getResources().getColor(R.color.colorAccent))
                        .backgroundColor(getResources().getColor(R.color.colorAccent))
                        .animated(false)
                        .build()
                        .show();

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

        RLMain.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (!post_type.equals("0") && !post_type.equals("2")) {
                    int heightDiff = RLMain.getRootView().getHeight() - RLMain.getHeight();
                    if (heightDiff > AppConstants.dpToPx(PostfeedActivity.this, 200)) {
//
                        btn_fullscren.setVisibility(View.GONE);


// Gets the layout params that will allow you to resize the layout

                        rlEditext.animate()
                                .translationY(-300)


                                .alpha(7.0f)

                                .setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                        rlEditext.setBackground(getResources().getDrawable(R.drawable.postbackgroung));

                                        caption_txt.setPadding(10, 10, 10, 0);

                                        RL_imagefeed.requestLayout();
                                        RL_imagefeed.getLayoutParams().height = ((int) getResources().getDimension(R.dimen._100sdp));

                                    }
                                });


                    } else {
                        rlEditext.animate()
                                .translationY(0)

                                .alpha(1.0f)

                                .setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                        RL_imagefeed.requestLayout();
                                        btn_fullscren.setVisibility(View.VISIBLE);
                                        if (post_type.equals("2")) {
                                            // RL_imagefeed.getLayoutParams().height = ((int) getResources().getDimension(R.dimen._120sdp));
                                        } else {
                                            RL_imagefeed.getLayoutParams().height = ((int) getResources().getDimension(R.dimen._200sdp));
                                        }
                                        RL_imagefeed.setBackground(null);
                                    }
                                });
                        Log.e("KEYBOARD", "HIDE");


                    }
                }
            }
        });
        rlpublic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SimpleTooltip.Builder(PostfeedActivity.this)
                        .anchorView(rlpublic)
                        .text("Contact")
                        .gravity(Gravity.TOP)

                        .arrowColor(getResources().getColor(R.color.colorAccent))
                        .backgroundColor(getResources().getColor(R.color.colorAccent))
                        .animated(false)
                        .build()
                        .show();
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

        imgRateuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = "<font color=#3F51B5>@</font>";
                caption_txt.append(Html.fromHtml(text));
            }
        });
        imgMention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = "<font color=#3F51B5>#</font>";
                caption_txt.append(Html.fromHtml(text));


            }
        });

        rlcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new SimpleTooltip.Builder(PostfeedActivity.this)
                        .anchorView(rlcard)
                        .text("Member")
                        .gravity(Gravity.TOP)
                        .arrowColor(getResources().getColor(R.color.colorAccent))
                        .backgroundColor(getResources().getColor(R.color.colorAccent))

                        .animated(false)
                        .build()
                        .show();
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
            setupTagAutocomplete();

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
                String min = gettimeinminute(mediaPlayer.getDuration());
                strdur = millisecondsToTime(mediaPlayer.getDuration());
////
                duration = Integer.parseInt(strdur);
                txtduration.setText(min);

//
            }
        });

        linbar.getBackground().setAlpha(150);
        ib_play_pause.getBackground().setAlpha(150);
        txtduration.getBackground().setAlpha(150);

        ib_play_pause.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("Range")
            @Override
            public void onClick(View v) {

                if (mediaPlayer != null) {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                        timer.cancel();
                        Log.e("inelse123", "" + duration);

                        ib_play_pause.setImageDrawable(ContextCompat.getDrawable(
                                PostfeedActivity.this,
                                R.drawable.ic_play_red_48dp));
                    } else {
                        mediaPlayer.start();
//
                        timer = new Timer();
                        timer.schedule(new TimerTask() {
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Your code

                                        startDur++;
                                        if (startDur <= duration) {

                                            if (startDur <= 9) {
                                                txtduration.setText("00:0" + startDur);
                                            } else {
                                                txtduration.setText("00:" + startDur);
                                            }


                                        } else {
                                            timer.cancel();
                                            startDur = 0;
                                            Log.e("duration", "" + startDur);
                                            Log.e("duration12", "" + strdur);
                                        }

                                    }
                                });
                            }
                        }, 1000, 1000);


                        ib_play_pause.setImageDrawable(ContextCompat.getDrawable(
                                PostfeedActivity.this,
                                R.drawable.ic_pause_red_48dp));


                    }
                } else {

                    strdur = millisecondsToTime(mediaPlayer.getDuration());

                    Log.e("FileDURATION", ">>" + strdur);
                    duration = Integer.parseInt(strdur);
                    Log.e("inelse", "" + duration);
                    mediaPlayer.seekTo(0);
//                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // Your code

                                    duration--;
                                    if (duration >= 0) {

                                        if (duration <= 9) {
                                            txtduration.setText("00:0" + duration);
                                        } else {
                                            txtduration.setText("00:" + duration);
                                        }


                                    } else {
                                        timer.cancel();
                                        Log.e("duration", "" + duration);
                                        Log.e("duration12", "" + strdur);
                                    }

                                }
                            });
                        }
                    }, 1000, 1000);
                    ib_play_pause.setImageDrawable(ContextCompat.getDrawable(
                            PostfeedActivity.this,
                            R.drawable.ic_pause_red_48dp));

                }


//
            }
        });


        projectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView) adapterView.getChildAt(0)).setTypeface(customFonts.calibri);
                projectID = projectList.get(i).getObjectId();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        Log.e("strFile", strFile);
        // post_type="2";


        if (post_type.equals("0")) {
            btn_fullscren.setVisibility(View.GONE);
            RL_imagefeed.setVisibility(View.GONE);

        } else if (post_type.equals("2")) {

            RL_imagefeed.setVisibility(View.VISIBLE);
            RL_imagefeed.requestLayout();
            RL_imagefeed.getLayoutParams().height = ((int) getResources().getDimension(R.dimen._120sdp));
            File file = new File(strFile);

            strdur = millisecondsToTime(mediaPlayer.getDuration());

            Log.e("FileDURATION", ">>" + strdur);
            duration = Integer.parseInt(strdur);

            String min = gettimeinminute(mediaPlayer.getDuration());

            txtduration.setText(min);


            converttexttospeech(file, strFile, post_type);


        } else if (post_type.equals("1")) {

            RL_imagefeed.setVisibility(View.VISIBLE);
            imgCapturedimage.setVisibility(View.VISIBLE);
            include_visual.setVisibility(View.GONE);
            rl_vidio.setVisibility(View.GONE);

            File file = new File(strFile);

            if (!strFile.equals("")) {

                setImage(strFile);
//
            } else {
                RL_imagefeed.setVisibility(View.GONE);
            }
        } else if (post_type.equals("3")) {
            RL_imagefeed.setVisibility(View.VISIBLE);

            rl_vidio.setVisibility(View.VISIBLE);
            imgCapturedimage.setVisibility(View.GONE);
            include_visual.setVisibility(View.GONE);
            jzVideoPlayerStandard.dismissVolumeDialog();
            jzVideoPlayerStandard.setUp(strFile
                    , JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "");
//
        } else if (post_type.equals("4")) {
            RL_imagefeed.setVisibility(View.VISIBLE);
            imgCapturedimage.setVisibility(View.VISIBLE);
            include_visual.setVisibility(View.VISIBLE);
            rl_vidio.setVisibility(View.GONE);


            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RL_imagefeed.getId());
            include_visual.setLayoutParams(params);
            // callimagewidAudio();
            if (!strFile.equals("")) {
                setImage(imageURL);

            } else {
                RL_imagefeed.setVisibility(View.GONE);
            }
            strdur = millisecondsToTime(mediaPlayer.getDuration());
            duration = Integer.parseInt(strdur);

            String min = gettimeinminute(mediaPlayer.getDuration());

            txtduration.setText(min);
            File file = new File(strFile);
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
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
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

                } else if (post_type.equals("4")) {
                    byte[] soundBytes;
                    try {
                        InputStream inputStream =
                                getContentResolver().openInputStream(Uri.fromFile(new File(strFile)));
                        //  soundBytes = new byte[inputStream.available()];
                        soundBytes = toByteArray(inputStream);

                        ParseFile audioFile = new ParseFile("file.m4a", soundBytes);


                        File newFile = new File(imageURL);
                        Uri selectedImage = Uri.fromFile(newFile);


                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(PostfeedActivity.this.getContentResolver(), selectedImage);

                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        // Compress image to lower quality scale 1 - 100
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
                        byte[] image = stream.toByteArray();


                        // Create the ParseFile

                        parseFile = new ParseFile("postfile.jpg", image);


                        createPostImageWithaudio(parseFile, audioFile);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }

            }
        });


        btn_fullscren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editIcon) {
                    btn_fullscren.setImageDrawable(getResources().getDrawable(R.mipmap.compress_icon));

                    RL_imagefeed.animate()
                            .translationY(-500)


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

    private void findview() {
        strMentionList = new ArrayList<>();
        subObject = new JSONObject();
        aq = new AQuery(PostfeedActivity.this);
        reply = (ImageButton) findViewById(R.id.reply);
        strFile = getIntent().getStringExtra("file");
        post_type = getIntent().getStringExtra("post_type");
        imageURL = getIntent().getStringExtra("imageURL");
        rlmember = (RelativeLayout) findViewById(R.id.rlmember);
        rlSingleUser = (RelativeLayout) findViewById(R.id.rlSingleUser);
        customFonts = new CustomFonts(PostfeedActivity.this);
        rlcard = (RelativeLayout) findViewById(R.id.rlcard);
        RLMain = (RelativeLayout) findViewById(R.id.RLMain);
        rlpublic = (RelativeLayout) findViewById(R.id.rlpublic);
        Log.e("strFile", strFile + ">>>" + post_type + ">>>>" + imageURL);
        projectSpinner = (Spinner) findViewById(R.id.projectSpinner);
        jzVideoPlayerStandard = (JZVideoPlayerStandard) findViewById(R.id.jzVideoPlayerStandard);
        UserList = new ArrayList<>();
        ll_privecy = (LinearLayout) findViewById(R.id.ll_privecy);
        ll_bottm = (LinearLayout) findViewById(R.id.ll_bottm);
        llPlaybtn = (LinearLayout) findViewById(R.id.llPlaybtn);
        btnVolume = (ImageView) findViewById(R.id.btnVolume);
        imgSingleUser = (ImageView) findViewById(R.id.imgSingleUser);
        img_arroe = (ImageView) findViewById(R.id.img_arroe);
        rlEditext = (LinearLayout) findViewById(R.id.rlEditext);
        workid = AppConstants.loadPreferences(PostfeedActivity.this, "workid");
        projectID = AppConstants.loadPreferences(PostfeedActivity.this, "projectID");
        include_visual = (RelativeLayout) findViewById(R.id.include_visual);
        lineBarVisualizer = (LineBarVisualizer) findViewById(R.id.visualizer);
        imgpublic = (ImageView) findViewById(R.id.imgpublic);
        imgcard = (ImageView) findViewById(R.id.imgcard);
        imgPrivacy = (ImageView) findViewById(R.id.imgPrivacy);
        imgBinoculor = (ImageView) findViewById(R.id.imgBinoculor);
        imgRateuser = (ImageView) findViewById(R.id.imgRateuser);
        imgMention = (ImageView) findViewById(R.id.imgMention);
        caption_txt = (EditText) findViewById(R.id.caption_txt);
        projectList = new ArrayList<>();
        txtduration = (TextView) findViewById(R.id.txtduration);
        btn_sendPost = (TextView) findViewById(R.id.btn_sendPost);
        post_header = (TextView) findViewById(R.id.post_header);
        imgCapturedimage = (ImageView) findViewById(R.id.imagesPost);

        btn_fullscren = (ImageView) findViewById(R.id.btn_fullscren);
        rl_vidio = (RelativeLayout) findViewById(R.id.rl_vidio);
        post_header.setTypeface(customFonts.CabinBold);
        ib_play_pause = (ImageButton) findViewById(R.id.ib_play_pause);
        RL_imagefeed = (RelativeLayout) findViewById(R.id.RL_imagefeed);
        txtduration.setTypeface(customFonts.calibri);
        btn_sendPost.setTypeface(customFonts.calibri);
        linbar = (RelativeLayout) findViewById(R.id.linbar);
    }

    private void setupTagAutocomplete() {

        float elevation = 6f;
        Drawable backgroundDrawable = new ColorDrawable(Color.WHITE);
        AutocompletePolicy policy = new CharPolicy('#'); // Look for #mentions
        AutocompletePresenter<ProjctBean> presenter = new ProjectPresenter(PostfeedActivity.this);
        AutocompleteCallback<ProjctBean> callback = new AutocompleteCallback<ProjctBean>() {
            @Override
            public boolean onPopupItemClicked(Editable editable, ProjctBean item) {
                // Replace query text with the full name.
                int[] range = CharPolicy.getQueryRange(editable);
                if (range == null) return false;
                int start = range[0];
                int end = range[1];
                String replacement = item.getName();
                editable.replace(start, end, replacement);
                editable.setSpan(new ForegroundColorSpan(Color.BLUE), start, start + replacement.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                editable.setSpan(new TextAppearanceSpan(PostfeedActivity.this, R.style.MyTheme), start, start + replacement.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                return true;
            }

            public void onPopupVisibilityChanged(boolean shown) {

            }
        };

        mentionsAutocomplete = Autocomplete.<ProjctBean>on(caption_txt)
                .with(elevation)
                .with(backgroundDrawable)

                .with(policy)
                .with(presenter)
                .with(callback)
                .build();
    }

    private String gettimeinminute(int duration) {

        long minutes = (duration / 1000) / 60;
        long seconds = (duration / 1000) % 60;
        String secondsStr = Long.toString(seconds);
        String secs;
        if (secondsStr.length() >= 2) {
            secs = secondsStr.substring(0, 2);
        } else {
            secs = "0" + secondsStr;
        }

        return "0" + minutes + ":" + secs;
    }


    public void createPostImageWithaudio(ParseFile parseFile, ParseFile audioFile) {


        ParseObject gameScore = new ParseObject("Post");

        gameScore.put("user", ParseObject.createWithoutData("_User", ParseUser.getCurrentUser().getObjectId()));
        gameScore.put("text", caption_txt.getText().toString());
        gameScore.put("workspace", ParseObject.createWithoutData("WorkSpace", workid));
        gameScore.put("project", ParseObject.createWithoutData("Project", projectID));
        gameScore.put("post_type", post_type);
        gameScore.put("CommentCount", 0);
        gameScore.put("likesCount", 0);
        gameScore.put("privacy", privacy);
        gameScore.put("post_type", post_type);
        gameScore.put("postImage", parseFile);
        gameScore.put("media_duration", HomeFeedActivity.prog + "");
        gameScore.put("post_file", audioFile);
//        gameScore.put("audioWave", wave);
        gameScore.put("tagUserId", subObject.toString());

        gameScore.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (e == null) {
                    Toast.makeText(PostfeedActivity.this, "Post Share", Toast.LENGTH_SHORT).show();
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



    private String millisecondsToTime(int duration) {
        long minutes = (duration / 1000) / 60;
        long seconds = (duration / 1000) % 60;
        String secondsStr = Long.toString(seconds);
        String secs;
        if (secondsStr.length() >= 2) {
            secs = secondsStr.substring(0, 2);
        } else {
            secs = "0" + secondsStr;
        }

        return secs;
    }

    @Override
    public void onBackPressed() {
        if (jzVideoPlayerStandard.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        jzVideoPlayerStandard.releaseAllVideos();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }


    private void converttexttospeech(final File file, final String strFile, final String woImage) {
        final ACProgressFlower acProgressFlower = new ACProgressFlower.Builder(PostfeedActivity.this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)

                .fadeColor(Color.DKGRAY).build();

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
//

        projectList.clear();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Project");

        query.whereEqualTo("workspace", ParseObject.createWithoutData("WorkSpace", workid));
        query.include("user");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    try {

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

                        for (int a = 0; a < projectList.size(); a++) {
                            if (projectList.get(a).getObjectId().equals(projectID)) {
                                projectSpinner.setSelection(a);
                            }

                        }


                    } catch (NullPointerException ea) {
                        ea.printStackTrace();
                    }
                } else {

                    // error
                }



                getprojectmember();
                setupMentionsAutocomplete();

            }
        });

    }

    private void setupMentionsAutocomplete() {

        float elevation = 6f;
        Drawable backgroundDrawable = new ColorDrawable(Color.WHITE);
        AutocompletePolicy policy = new CharPolicy('@'); // Look for @mentions
        AutocompletePresenter<WorkSpaceMemberBean> presenter = new UserPresenter(PostfeedActivity.this);
        AutocompleteCallback<WorkSpaceMemberBean> callback = new AutocompleteCallback<WorkSpaceMemberBean>() {
            @Override
            public boolean onPopupItemClicked(Editable editable, WorkSpaceMemberBean item) {
                // Replace query text with the full name.

                JSONObject jsonObject = new JSONObject();


                if (!strMentionList.contains(item.getObjectId())) {
                    try {
                        strMentionList.add(item.getObjectId());
                        subObject.put(item.getFullname(), item.getObjectId());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                int[] range = CharPolicy.getQueryRange(editable);
                if (range == null) return false;
                int start = range[0];
                int end = range[1];
                String replacement = item.getFullname();
                editable.replace(start, end, replacement);

                // This is better done with regexes and a TextWatcher, due to what happens when
                // the user clears some parts of the text. Up to you.
//                editable.setSpan(new ForegroundColorSpan(Color.BLUE), start, start + replacement.length(),
//                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                editable.setSpan(new ForegroundColorSpan(Color.BLUE), start, start + replacement.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                editable.setSpan(new TextAppearanceSpan(PostfeedActivity.this, R.style.MyTheme), start, start + replacement.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                return true;
            }

            public void onPopupVisibilityChanged(boolean shown) {
            }
        };

        mentionsAutocomplete = Autocomplete.<WorkSpaceMemberBean>on(caption_txt)
                .with(elevation)
                .with(backgroundDrawable)
                .with(policy)
                .with(presenter)
                .with(callback)
                .build();
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


                            UserList.add(new WorkSpaceMemberBean(objectId, username, email, updatedAt, createdAt, passion, img, title, fullname
                                    , campanyrole, communityrole, address));
                        }


                    }

                } else {
                    Toast.makeText(PostfeedActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    Log.e("else", e.getMessage());
                    // error
                }
                ll_bottm.setVisibility(View.VISIBLE);
//


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
            if (post_type.equals("2") || post_type.equals("4")) {
                setPlayer();
            }
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


    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        switch (requestCode) {
            case AUDIO_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (post_type.equals("2") || post_type.equals("4")) {
                        setPlayer();
                    }
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
        gameScore.put("tagUserId", subObject.toString());

        if (post_type.equals("1")) {
            if (parseFile != null) {
                gameScore.put("postImage", parseFile);
            }
        } else if (post_type.equals("2")) {
            gameScore.put("post_file", parseFile);
            gameScore.put("media_duration", HomeFeedActivity.prog + "");
        } else if (post_type.equals("3")) {
            gameScore.put("post_file", parseFile);

        }

        gameScore.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (e == null) {
                    Toast.makeText(PostfeedActivity.this, "Post Share", Toast.LENGTH_SHORT).show();
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
            aq.id(imgCapturedimage).image(newFile, true, 500, cb);
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
            make.setTypeface(customFonts.calibri);
            make.setText(patientlist.get(position).getName());

            return row;

        }
    }


}

