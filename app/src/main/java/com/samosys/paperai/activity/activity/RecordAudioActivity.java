package com.samosys.paperai.activity.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.samosys.paperai.R;
import com.samosys.paperai.activity.AudioRecord.RecordingService;
import com.samosys.paperai.activity.utils.AppConstants;
import com.samosys.paperai.activity.utils.CircularProgressBar;
import com.samosys.paperai.activity.utils.CustomFonts;
import com.samosys.paperai.activity.utils.MarshMallowPermission;
import com.samosys.paperai.activity.utils.SimpleTooltip;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class RecordAudioActivity extends AppCompatActivity {
    public static File file = null;
    ImageView imgCapturedimage, img_mic, imgback_signup;
    String strFile;
    AQuery aq;
    int prog = 0;
    TextView txt_next_audio, txtheader;
    Intent recordService;
    Timer timer;
    CustomFonts customFonts;
    CircularProgressBar circularProgressbar;
    MarshMallowPermission marshMallowPermission;
    private boolean mStartRecording = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_record_audio);
        customFonts = new CustomFonts(RecordAudioActivity.this);
        txt_next_audio = (TextView) findViewById(R.id.txt_next_audio);
        txtheader = (TextView) findViewById(R.id.txtheader);
        imgCapturedimage = (ImageView) findViewById(R.id.imgCapturedimage);
        img_mic = (ImageView) findViewById(R.id.img_mic);
        txtheader = (TextView) findViewById(R.id.txtheader);
        txtheader.setTypeface(customFonts.CabinBold);
        marshMallowPermission = new MarshMallowPermission(RecordAudioActivity.this);
        imgback_signup = (ImageView) findViewById(R.id.imgback_signup);
        circularProgressbar = (CircularProgressBar) findViewById(R.id.circularProgressbar);
        aq = new AQuery(RecordAudioActivity.this);
        strFile = getIntent().getStringExtra("file");
        File newFile = new File(strFile);
        String workname = AppConstants.loadPreferences(RecordAudioActivity.this, "workname");
        txtheader.setText(workname);

        new SimpleTooltip.Builder(this)
                .anchorView(circularProgressbar)
                .text("Press hold to record your voice")
                .gravity(Gravity.TOP)


                .animated(true)
                .build()
                .show();
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
                            cb.targetWidth(1000).rotate(true);
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
        txt_next_audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecordAudioActivity.this, PostfeedActivity.class);
                intent.putExtra("file", strFile);
                intent.putExtra("post_type", "1");

                startActivity(intent);
            }
        });
//        img_mic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(RecordAudioActivity.this, "working", Toast.LENGTH_SHORT).show();
//            }
//        });

        img_mic.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (!marshMallowPermission.checkPermissionForRecord()) {
                        marshMallowPermission.requestPermissionForRecord();
                    } else {
                        //  showDialog();
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:


                                prog = 0;
                                //  if (prog == 1) {

                                onRecord(mStartRecording);
                                mStartRecording = !mStartRecording;
                                //}
                                timer = new Timer();
                                timer.schedule(new TimerTask() {
                                    public void run() {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                // Your code

                                                prog++;
                                                if (prog <= 60) {
                                                    circularProgressbar.setProgress(prog);
                                                    Log.e("PROGRESS22", prog + "");

                                                } else {

                                                    circularProgressbar.setProgress(0);
                                                    stopRecording();

                                                    timer.cancel();
                                                }
                                            }
                                        });
                                    }
                                }, 1000, 1000);


                                return true; // if you want to handle the touch event
                            case MotionEvent.ACTION_UP:
                                //    prog = 0;
                                timer.cancel();

                                Log.e("PROGRESS11", prog + "");
//                                circularProgressbar.setProgress(0);
                                stopRecording();
                                // RELEASED
                                return true; // if you want to handle the touch event

                        }
                    }
                } else {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:


                            prog = 0;
                            // if (prog == 1) {

                            onRecord(mStartRecording);
                            mStartRecording = !mStartRecording;
                            // }
                            timer = new Timer();
                            timer.schedule(new TimerTask() {
                                public void run() {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            // Your code

                                            prog++;
                                            if (prog <= 60) {
                                                circularProgressbar.setProgress(prog);
                                                Log.e("PROGRESS22", prog + "");

                                            } else {
//                                                pro_free.setVisibility(View.VISIBLE);
//                                                img_addpost.setVisibility(View.VISIBLE);
//                                                rl_audioRecord.setVisibility(View.INVISIBLE);

                                                circularProgressbar.setProgress(0);
                                                stopRecording();
                                                timer.cancel();
                                            }
                                        }
                                    });
                                }
                            }, 1000, 1000);


                            return true; // if you want to handle the touch event
                        case MotionEvent.ACTION_UP:
                            //+  prog = 0;
                            timer.cancel();
                            stopService(recordService);
//                            pro_free.setVisibility(View.VISIBLE);

//                            img_addpost.setVisibility(View.VISIBLE);
//                            rl_audioRecord.setVisibility(View.INVISIBLE);
                            Log.e("PROGRESS11", prog + "");
                            circularProgressbar.setProgress(0);
                            stopRecording();
                            // RELEASED
                            return true; // if you want to handle the touch event

                    }
                }


                return false;

            }

        });

        imgback_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecordAudioActivity.this, CameraPostActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(RecordAudioActivity.this, CameraPostActivity.class);
        startActivity(intent);
        finish();
    }

    private void stopRecording() {
        //  if (file != null) {

        stopService(recordService);


        //}
    }

    private void onRecord(boolean mStartRecording) {


        recordService = new Intent(RecordAudioActivity.this, RecordingService.class);

        recordService.putExtra("image", "yes");
        recordService.putExtra("imageURL", strFile);


        File folder = new File(Environment.getExternalStorageDirectory() + "/SoundRecorder");
        if (!folder.exists()) {
            //folder /SoundRecorder doesn't exist, create the folder
            folder.mkdir();
        }

        //start Chronometer

        //start RecordingService
        startService(recordService);
        //keep screen on while recording
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


    }
}