package com.samosys.paperai.activity.AudioRecord;

import android.app.Service;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import com.samosys.paperai.R;
import com.samosys.paperai.activity.activity.HomeFeedActivity;
import com.samosys.paperai.activity.activity.PostfeedActivity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * Created by Daniel on 12/28/2014.
 */
public class RecordingService extends Service {

    private static final String LOG_TAG = "RecordingService";
    private static final SimpleDateFormat mTimerFormat = new SimpleDateFormat("mm:ss", Locale.getDefault());

    private String mFileName = null;
    private String mFilePath = null;
    private MediaRecorder mRecorder = null;
    private long mStartingTimeMillis = 0;
    private long mElapsedMillis = 0;
    private int mElapsedSeconds = 0;
    private Timer mTimer = null;
    private TimerTask mIncrementTimerTask = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startRecording();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();


        if (mRecorder != null) {


           // stopRecording();
        }


    }

    public void startRecording() {
        setFileNameAndPath();




        mRecorder = new MediaRecorder();

        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mRecorder.setAudioChannels(1);
        mRecorder.setAudioEncodingBitRate(32000);
        mRecorder.setAudioSamplingRate(16000);

        mRecorder.setOutputFile(mFilePath);

        try {
            mRecorder.prepare();
            mRecorder.start();
            mStartingTimeMillis = System.currentTimeMillis();

            //startTimer();
            //startForeground(1, createNotification());

        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    public void setFileNameAndPath() {
        int count = 0;


        do {
            Calendar cal = Calendar.getInstance();
            long millisecondOfDay =
                    TimeUnit.HOURS.toMillis(cal.get(Calendar.HOUR_OF_DAY)) +
                            TimeUnit.MINUTES.toMillis(cal.get(Calendar.MINUTE));
            count++;

            mFileName = getString(R.string.app_name)
                    + "_" + millisecondOfDay + ".mp3";
            mFilePath = Environment.getExternalStorageDirectory().getAbsolutePath();
            mFilePath += "/SoundRecorder/" + mFileName;

            HomeFeedActivity.file = new File(mFilePath);
        } while (HomeFeedActivity.file.exists() && !HomeFeedActivity.file.isDirectory());
    }

    public void stopRecording() {


        mRecorder.stop();
        mElapsedMillis = (System.currentTimeMillis() - mStartingTimeMillis);
        mRecorder.release();
//        Toast.makeText(this, getString(R.string.app_name) + " " + mFilePath, Toast.LENGTH_LONG).show();
        Log.e("SoundRecorder", mFilePath);
        Intent i = new Intent(this, PostfeedActivity.class);
        i.putExtra("file", HomeFeedActivity.file.toString());
        i.putExtra("post_type", "2");

        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(i);

        mRecorder = null;


    }

}
