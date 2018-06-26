package com.samosys.paperai.activity.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.samosys.paperai.R;
import com.samosys.paperai.activity.fragment.MyprofileBookmarkFragment;
import com.samosys.paperai.activity.fragment.MyprofilePostFragment;
import com.samosys.paperai.activity.utils.AppConstants;
import com.samosys.paperai.activity.utils.CustomFonts;
import com.samosys.paperai.activity.utils.First_Char_Capital;

import java.util.List;

public class MyProfileActivity extends AppCompatActivity {
    CustomFonts customFonts;
    private String user_id = "";
    private ImageView my_img, img_mybookmark, img_mypost, imgSetting;
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
        imgSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyProfileActivity.this, EditMyProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getuserdata();
    }

    private void getuserdata() {

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("objectId", ParseUser.getCurrentUser().getObjectId());
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); i++) {
                        String objectId = objects.get(i).getObjectId();
                        String username = objects.get(i).getString("username");
                        String email = objects.get(i).getString("email");
                        Log.e("EMAIL_USER", email + ">>" + username);

                        String updatedAt = objects.get(i).getUpdatedAt().toString();
                        String createdAt = objects.get(i).getCreatedAt().toString();
                        String passion = objects.get(i).getString("passion");
                        ParseFile profileimage = (ParseFile) objects.get(i).get("profileimage");
                        String title = objects.get(i).getString("title");
                        String fullname = objects.get(i).getString("fullname");
                        String campanyrole = objects.get(i).getString("campanyrole");
                        String communityrole = objects.get(i).getString("communityrole");
                        String address = objects.get(i).getString("address");
                        userName.setText(First_Char_Capital.capitalizeString(fullname));
                        my_postoion.setText(campanyrole);
//                        edtcommunityrole.setText(communityrole);
//                        edtAdress.setText(address);
//                        edtTitle.setText(title);
//                        edtpassion.setText(passion);
                        profileimage.getDataInBackground(new GetDataCallback() {
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
                } else {
                    Toast.makeText(MyProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void findview() {
        userName = (TextView) findViewById(R.id.userName);
        my_postoion = (TextView) findViewById(R.id.my_postoion);
        userName.setTypeface(customFonts.HelveticaNeueBold);
        my_postoion.setTypeface(customFonts.HelveticaNeueMedium);
        my_img = (ImageView) findViewById(R.id.my_img);
        imgSetting = (ImageView) findViewById(R.id.imgSetting);
        img_mybookmark = (ImageView) findViewById(R.id.img_mybookmark);
        img_mypost = (ImageView) findViewById(R.id.img_mypost);
        ll_mybookmark = (LinearLayout) findViewById(R.id.ll_mybookmark);
        ll_mypost = (LinearLayout) findViewById(R.id.ll_mypost);
    }
}
