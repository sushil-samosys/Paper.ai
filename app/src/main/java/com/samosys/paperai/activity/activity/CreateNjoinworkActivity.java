package com.samosys.paperai.activity.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.samosys.paperai.R;
import com.samosys.paperai.activity.utils.AppConstants;

public class CreateNjoinworkActivity extends AppCompatActivity {
TextView txtcreate,txtjoin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_njoinwork);
        AppConstants.getstatusbar(CreateNjoinworkActivity.this);
        txtcreate=(TextView)findViewById(R.id.txtcreate);
        txtjoin=(TextView)findViewById(R.id.txtjoin);

        txtjoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(CreateNjoinworkActivity.this,SearchWorkspaceActivity.class);
                startActivity(i);
            }
        });
        txtcreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(CreateNjoinworkActivity.this,NewWorkspaceActivity.class);
                startActivity(i);
            }
        });
    }

}
