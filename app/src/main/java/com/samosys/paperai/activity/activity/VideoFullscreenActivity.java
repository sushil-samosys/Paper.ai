package com.samosys.paperai.activity.activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.samosys.paperai.R;
import com.samosys.paperai.activity.utils.AppConstants;

import cc.cloudist.acplibrary.ACProgressFlower;

public class VideoFullscreenActivity extends AppCompatActivity implements
        MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener,
        MediaPlayer.OnVideoSizeChangedListener, SurfaceHolder.Callback, MediaController.MediaPlayerControl {
    String videoUrl = "";
    VideoView videoView;
    ImageView imgPlay;
    private MediaController mcontroller;
    ACProgressFlower acProgressFlower = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_fullscreen);
        AppConstants.getstatusbar(VideoFullscreenActivity.this);
        videoUrl = getIntent().getStringExtra("url");
        videoView = (VideoView) findViewById(R.id.videoViewFull);
        imgPlay = (ImageView) findViewById(R.id.imgPlay);
        acProgressFlower = AppConstants.CustomshowProgressDialog(VideoFullscreenActivity.this, "");
        acProgressFlower.show();
        if (!videoUrl.equals("")) {
            try {

                mcontroller = new MediaController(this);

                videoView.setMediaController(mcontroller);
                mcontroller.show();
                videoView.setVideoPath(videoUrl);
                videoView.start();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        Log.e("VIDEOLOAD",""+percent);

        if (percent <= 99) {


           // imgPlay.setVisibility(View.GONE);


        } else if (percent==100){

            if (acProgressFlower.isShowing()) {
                acProgressFlower.dismiss();
            }
           // imgPlay.setVisibility(View.GONE);

        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
       // imgPlay.setVisibility(View.VISIBLE);
        mcontroller.setEnabled(true);
        mcontroller.show();
    }

    @Override
    public void onPrepared(MediaPlayer m) {

        mcontroller.setEnabled(true);
        mcontroller.show();
        if (acProgressFlower.isShowing()) {
            acProgressFlower.dismiss();
        }
        try {
            if (m.isPlaying()) {
                m.stop();
                m.release();
                m = new MediaPlayer();
            }
            m.setVolume(0f, 0f);
            m.setLooping(false);
            m.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    public void start() {
        videoView.start();
    }

    public void pause() {
        videoView.pause();
        mcontroller.show();
    }

    public int getDuration() {
        return videoView.getDuration();
    }

    public int getCurrentPosition() {
        return videoView.getCurrentPosition();
    }

    public void seekTo(int i) {
        videoView.seekTo(i);
    }

    public boolean isPlaying() {
        mcontroller.show();
        return videoView.isPlaying();

    }

    public int getBufferPercentage() {
        return 0;
    }

    public boolean canPause() {
        return true;
    }

    public boolean canSeekBackward() {
        return true;
    }

    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {

        return 0;
    }
}