package com.samosys.paperai.activity.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.samosys.paperai.R;
import com.samosys.paperai.activity.utils.AppConstants;
import com.samosys.paperai.activity.utils.CustomFonts;

import java.util.ArrayList;
import java.util.List;

public class InviteMemberActivity extends AppCompatActivity {
    EditText edt_email_add, edtemaiFirt;
    LinearLayout linear;
    TextView txt, txt_skip,txt_invite;
    String work_id;
    CustomFonts customFonts;
    List<EditText> allEds = new ArrayList<EditText>();
    private int btn_email_id = 1784450;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_member);
        AppConstants.getstatusbar(InviteMemberActivity.this);
        customFonts = new CustomFonts(InviteMemberActivity.this);
        work_id=getIntent().getStringExtra("id");
        txt = (TextView) findViewById(R.id.txt);
        txt_invite = (TextView) findViewById(R.id.txt_invite);
        txt_skip = (TextView) findViewById(R.id.txt_skip);
        edtemaiFirt = (EditText) findViewById(R.id.edtemaiFirt);
        txt.setTypeface(customFonts.HelveticaNeue);
        txt_skip.setTypeface(customFonts.HelveticaNeue);
        txt_invite.setTypeface(customFonts.HelveticaNeue);
        txt_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InviteMemberActivity.this, NewProjctActivity.class);
                intent.putExtra("id", work_id);
                startActivity(intent);
            }
        });
        txt_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(InviteMemberActivity.this, "Working", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
