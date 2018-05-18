package com.samosys.paperai.activity.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.samosys.paperai.R;
import com.samosys.paperai.activity.utils.AppConstants;

public class SearchWorkspaceActivity extends AppCompatActivity {
    TextView txt_browse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_workspace);
        AppConstants.getTranparentstatusbar(SearchWorkspaceActivity.this);
        txt_browse = (TextView) findViewById(R.id.txt_browse);
        txt_browse = (TextView) findViewById(R.id.txt_browse);

        txt_browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SearchWorkspaceActivity.this, BrowesWorkspace.class);
                startActivity(i);
            }
        });
    }
}
