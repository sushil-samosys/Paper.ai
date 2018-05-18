package com.samosys.paperai.activity.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.samosys.paperai.R;
import com.samosys.paperai.activity.utils.AppConstants;
import com.samosys.paperai.activity.utils.First_Char_Capital;
import com.squareup.picasso.Picasso;

public class OtherUserProfileActivity extends AppCompatActivity {
    private ImageView other_user_img;
    private String id;
    private TextView userName, other_user_postoion, other_pro_header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user_profile);
        AppConstants.getstatusbar(OtherUserProfileActivity.this);
        other_user_img = (ImageView) findViewById(R.id.other_user_img);
        other_pro_header = (TextView) findViewById(R.id.other_pro_header);
        userName = (TextView) findViewById(R.id.userName);
        other_user_postoion = (TextView) findViewById(R.id.other_user_postoion);
        id = getIntent().getStringExtra("id");

        getuserdata();
    }

    private void getuserdata() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
        query.getInBackground(id, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {

                    String fullname = object.getString("fullname");
                    ParseFile image = object.getParseFile("profileimage");
                    String imageUrl = image.getUrl();

                    String title = object.getString("title");
                    String passion = object.getString("passion");

                    other_pro_header.setText(First_Char_Capital.capitalizeString(fullname));
                    userName.setText(First_Char_Capital.capitalizeString(fullname));
                    other_user_postoion.setText(First_Char_Capital.capitalizeString(title));

                    if (image != null) {
                        Picasso.with(OtherUserProfileActivity.this)
                                .load(imageUrl)
                                .fit()
                                .centerCrop()

                                .into(other_user_img);
//                        image.getDataInBackground(new GetDataCallback() {
//                            @Override
//                            public void done(byte[] data, ParseException e) {
//
//                                Bitmap bmp = BitmapFactory
//                                        .decodeByteArray(
//                                                data, 0,
//                                                data.length);
//                                Log.e("OTHEr_id", bmp+"");
//                                // initialize
//                                //other_user_img.setImageBitmap(bmp);
//
//
//                            }
//                        });
                    }


                    // object will be your game score
                } else {
                    Log.e("other_user_e", e.getMessage());
                    // something went wrong
                }
            }
        });

    }
}
