package com.samosys.paperai.activity.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.samosys.paperai.R;
import com.samosys.paperai.activity.utils.App;
import com.samosys.paperai.activity.utils.AppConstants;
import com.samosys.paperai.activity.utils.ZoomableImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;

import cc.cloudist.acplibrary.ACProgressFlower;

public class FullImageActivity extends AppCompatActivity {
    ZoomableImageView imageView;
    AQuery aq;
    String Imageurl;
    ACProgressFlower acProgressFlower=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);
        aq = new AQuery(FullImageActivity.this);
        AppConstants.getstatusbar(FullImageActivity.this);
        Imageurl = getIntent().getStringExtra("url");
        imageView = (ZoomableImageView) findViewById(R.id.image);
        Log.e("POSTURL1221", Imageurl);
         acProgressFlower= AppConstants.CustomshowProgressDialog(FullImageActivity.this,"");
         acProgressFlower.show();

        Picasso.with(FullImageActivity.this).load(Imageurl).error(R.mipmap.sign_up_workspace)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        acProgressFlower.dismiss();

                    }

                    @Override
                    public void onError() {
                        acProgressFlower.dismiss();;
                    }
                });

    }
}
