package com.samosys.paperai.activity.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.samosys.paperai.R;
import com.samosys.paperai.activity.fragment.MyprofileBookmarkFragment;
import com.samosys.paperai.activity.fragment.MyprofilePostFragment;
import com.samosys.paperai.activity.utils.AppConstants;
import com.samosys.paperai.activity.utils.CustomFonts;

public class MyProfileActivity extends AppCompatActivity {
    CustomFonts customFonts;
    private String user_id = "";
    private ImageView my_img,img_mybookmark,img_mypost;
    private TextView userName, my_postoion;
    private LinearLayout ll_mypost, ll_mybookmark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        customFonts = new CustomFonts(MyProfileActivity.this);
        AppConstants.getTranparentstatusbar(MyProfileActivity.this);

        findview();
    //    user_id = getIntent().getStringExtra("id");
        getuserdata();

        MyprofilePostFragment prospectFragment = new MyprofilePostFragment();
        android.support.v4.app.FragmentTransaction homefragmentTransaction = getSupportFragmentManager().beginTransaction();
        homefragmentTransaction.add(R.id.fragment_container, prospectFragment);
        homefragmentTransaction.commit();
        ll_mypost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_mybookmark.setImageDrawable(getResources().getDrawable(R.mipmap.home_bookmark));
                img_mypost.setImageDrawable(getResources().getDrawable(R.mipmap.myprofile_post));

                MyprofilePostFragment prospectFragment = new MyprofilePostFragment();
                android.support.v4.app.FragmentTransaction homefragmentTransaction = getSupportFragmentManager().beginTransaction();
                homefragmentTransaction.replace(R.id.fragment_container, prospectFragment);
                homefragmentTransaction.commit();
            }
        });
        ll_mybookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_mybookmark.setImageDrawable(getResources().getDrawable(R.mipmap.home_bookmark_active));
                img_mypost.setImageDrawable(getResources().getDrawable(R.mipmap.home_share_inactive));
                MyprofileBookmarkFragment prospectFragment = new MyprofileBookmarkFragment();
                android.support.v4.app.FragmentTransaction homefragmentTransaction = getSupportFragmentManager().beginTransaction();
                homefragmentTransaction.replace(R.id.fragment_container, prospectFragment);
                homefragmentTransaction.commit();
            }
        });
    }

    private void getuserdata() {

        userName.setText(ParseUser.getCurrentUser().getUsername());
        if (ParseUser.getCurrentUser().getParseFile("profileimage") != null) {
            ParseUser.getCurrentUser().getParseFile("profileimage").getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    Bitmap bmp = BitmapFactory
                            .decodeByteArray(
                                    data, 0,
                                    data.length);

                    // initialize
                    my_img.setImageBitmap(bmp);
                }
            });
        }


    }

    private void findview() {
        userName = (TextView) findViewById(R.id.userName);
        my_postoion = (TextView) findViewById(R.id.my_postoion);
        userName.setTypeface(customFonts.HelveticaNeueBold);
        my_postoion.setTypeface(customFonts.HelveticaNeueMedium);
        my_img = (ImageView) findViewById(R.id.my_img);
        img_mybookmark = (ImageView) findViewById(R.id.img_mybookmark);
        img_mypost = (ImageView) findViewById(R.id.img_mypost);
        ll_mybookmark = (LinearLayout) findViewById(R.id.ll_mybookmark);
        ll_mypost = (LinearLayout) findViewById(R.id.ll_mypost);
    }
}
