package com.samosys.paperai.activity.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.samosys.paperai.R;
import com.samosys.paperai.activity.utils.AppConstants;
import com.samosys.paperai.activity.utils.NetworkAvailablity;
import com.samosys.paperai.activity.utils.SwitchButton;

import java.util.List;

import cc.cloudist.acplibrary.ACProgressFlower;

public class ArchiveProjectActivity extends AppCompatActivity {
    private SwitchButton ProtoglBtn;
    private TextView txtsave_pro;
    private String toggle = "", proID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive_project2);
        AppConstants.getstatusbar(ArchiveProjectActivity.this);
        ProtoglBtn = (SwitchButton) findViewById(R.id.ProtoglBtn);
        txtsave_pro = (TextView) findViewById(R.id.txtsave_pro);
        proID = getIntent().getStringExtra("id");

        ProtoglBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    toggle = "1";

                    Log.e("toggle>>", ">" + toggle);
                } else {
                    toggle = "0";

                    Log.e("toggle22>>", "22>" + toggle);

                }
            }
        });

        txtsave_pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toggle.equals("1")) {
                    archiveproject();
                }
            }
        });
    }

    private void archiveproject() {
        final ACProgressFlower acProgressFlower = AppConstants.CustomshowProgressDialog(ArchiveProjectActivity.this, "");
        acProgressFlower.show();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Project");
        query.whereEqualTo("objectId", proID);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    ParseObject person = list.get(0);
                    person.put("archive", "1");
                    if (NetworkAvailablity.chkStatus(ArchiveProjectActivity.this)) {
//                        getprojectlist();

                        if (acProgressFlower.isShowing()) {
                            acProgressFlower.dismiss();
                        }
                        Toast.makeText(ArchiveProjectActivity.this, "Archive Successfully", Toast.LENGTH_SHORT).show();
                        finish();


                    } else {
                        Toast.makeText(ArchiveProjectActivity.this, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                    }

                    person.saveInBackground();
                } else {
                    if (acProgressFlower.isShowing()) {
                        acProgressFlower.dismiss();
                    }
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });
    }
}
