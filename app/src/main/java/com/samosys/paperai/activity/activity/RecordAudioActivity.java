package com.samosys.paperai.activity.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.samosys.paperai.R;
import com.samosys.paperai.activity.utils.CircularProgressBar;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class RecordAudioActivity extends AppCompatActivity {
    ImageView imgCapturedimage, img_mic, imgback_signup;
    String strFile;
    AQuery aq;
    int prog = 0;
    TextView txt_next_audio;
    Timer timer;
    CircularProgressBar circularProgressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_record_audio);

        txt_next_audio = (TextView) findViewById(R.id.txt_next_audio);
        imgCapturedimage = (ImageView) findViewById(R.id.imgCapturedimage);
        img_mic = (ImageView) findViewById(R.id.img_mic);
        imgback_signup = (ImageView) findViewById(R.id.imgback_signup);
        circularProgressbar = (CircularProgressBar) findViewById(R.id.circularProgressbar);
        aq = new AQuery(RecordAudioActivity.this);
        strFile = getIntent().getStringExtra("file");
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
                startActivity(intent);
            }
        });
        img_mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RecordAudioActivity.this, "working", Toast.LENGTH_SHORT).show();
            }
        });

        img_mic.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        prog = 0;

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

                                            timer.cancel();
                                        }
                                    }
                                });
                            }
                        }, 1000, 1000);


                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        prog = 0;
                        timer.cancel();
                        Log.e("PROGRESS11", prog + "");
                        circularProgressbar.setProgress(prog);
                        // RELEASED
                        return true; // if you want to handle the touch event

                }

                return false;

            }

        });

        imgback_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}