package com.samosys.paperai.activity.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.samosys.paperai.R;
import com.samosys.paperai.activity.Bean.ProjctBean;
import com.samosys.paperai.activity.adapter.ProjectAdapter;
import com.samosys.paperai.activity.utils.AppConstants;

import java.util.ArrayList;
import java.util.List;

public class JoinProjectActivity extends AppCompatActivity {
    RecyclerView RV_project;
    EditText edtSerchproject;
    String id = "";
    ProjectAdapter adapter;
    ArrayList<ProjctBean> mylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_project);
        AppConstants.getstatusbar(JoinProjectActivity.this);
        AppConstants.keyboardhide(JoinProjectActivity.this);
        edtSerchproject = (EditText) findViewById(R.id.edtSerchproject);
        RV_project = (RecyclerView) findViewById(R.id.RV_project);
        mylist = new ArrayList<>();
        id = getIntent().getStringExtra("id");

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Project");
        query.whereEqualTo("workspace",ParseObject.createWithoutData("WorkSpace",id));
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {


                    for (int i = 0; i < objects.size(); i++) {
                        String objectId = objects.get(i).getObjectId();
                        String name = objects.get(i).getString("name");
                        String updatedAt = objects.get(i).getString("updatedAt");
                        String workspaceID = objects.get(i).getString("workspaceID");
                        String objective = objects.get(i).getString("objective");
                        String createdAt = objects.get(i).getString("createdAt");
                        String image = objects.get(i).getString("image");
                        String type = objects.get(i).getString("type");
                        String archive = objects.get(i).getString("archive");

                        mylist.add(new ProjctBean(objectId, name, updatedAt, workspaceID, objective, createdAt, image, type, objects.get(i), archive));
//
                        adapter = new ProjectAdapter(JoinProjectActivity.this, mylist, "");
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(JoinProjectActivity.this);
                        RV_project.setLayoutManager(layoutManager);

                        RV_project.setAdapter(adapter);


                        adapter.notifyDataSetChanged();

                    }

                } else {
                    Toast.makeText(JoinProjectActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    Log.e("lse", e.getMessage());
                    // error
                }
            }
        });

    }
}
