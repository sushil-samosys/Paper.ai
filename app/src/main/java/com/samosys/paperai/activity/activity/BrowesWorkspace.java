package com.samosys.paperai.activity.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import com.samosys.paperai.R;
import com.samosys.paperai.activity.Bean.WorkspaceBean;
import com.samosys.paperai.activity.adapter.SearchWorkspace;
import com.samosys.paperai.activity.utils.AppConstants;

import java.util.ArrayList;
import java.util.List;

public class BrowesWorkspace extends AppCompatActivity {
    RecyclerView rvworkspace;
    ArrayList<WorkspaceBean> workList;
    EditText edtSerchspace;
    SearchWorkspace adapter;
    ProgressDialog dialog;
    ImageView projectback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browes_workspace);
        AppConstants.getstatusbar(BrowesWorkspace.this);
        AppConstants.keyboardhide(BrowesWorkspace.this);
        rvworkspace = (RecyclerView) findViewById(R.id.rvworkspace);
        edtSerchspace = (EditText) findViewById(R.id.edtSerchspace);
        projectback = (ImageView) findViewById(R.id.projectback);
        workList = new ArrayList<>();
        workList.clear();
        dialog = AppConstants.showProgressDialog(BrowesWorkspace.this, "Loading");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("WorkSpace");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                Log.e("WorkSpace",  objects.size() + "");
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (e == null) {

                    for (int i = 0; i < objects.size(); i++) {

                        String objectId = objects.get(i).getObjectId();
                         ParseObject user = ParseObject.createWithoutData("WorkSpace", "user");
                        String mission = objects.get(i).getString("mission");
                        String updatedAt = objects.get(i).getUpdatedAt().toString();
                         String workspace_name = objects.get(i).getString("workspace_name");
                        String createdAt = objects.get(i).getCreatedAt().toString();
                         ParseFile image = (ParseFile) objects.get(i).get("image");
                        String ws_image = image.getUrl();
                        String user_name = objects.get(i).getString("user_name");
                        String workspace_url = objects.get(i).getString("workspace_url");
                        String archive = objects.get(i).getString("archive");

                         workList.add(new WorkspaceBean(objectId, user, mission, updatedAt, workspace_name, createdAt, image, ws_image, user_name, workspace_url, archive));
                    }
                    adapter = new SearchWorkspace(BrowesWorkspace.this, workList);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(BrowesWorkspace.this);
                    rvworkspace.setLayoutManager(layoutManager);
                    rvworkspace.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else {
                    Log.e("TOTAL", e.getMessage() + "");
                }
            }
        });

        projectback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        edtSerchspace.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                adapter.getFilter().filter(s.toString());
                adapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
