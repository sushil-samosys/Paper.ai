package com.samosys.paperai.activity.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.samosys.paperai.R;
import com.samosys.paperai.activity.Bean.ProjctBean;
import com.samosys.paperai.activity.adapter.ProjectAdapter;
import com.samosys.paperai.activity.utils.AppConstants;
import com.samosys.paperai.activity.utils.CustomFonts;
import com.samosys.paperai.activity.utils.NetworkAvailablity;

import java.util.ArrayList;
import java.util.List;

public class ProjectSettingActivity extends AppCompatActivity {
    CustomFonts customFonts;
    private String name = "", workID;
    private TextView txt_project_header;
    private ImageView img_project_back;
    private LinearLayout ll_add;
    private ArrayList<ProjctBean> projectList;
    private ProjectAdapter adapter;
    private RecyclerView recycleProject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_setting);
        AppConstants.getstatusbar(ProjectSettingActivity.this);
        findview();
        name = AppConstants.loadPreferences(ProjectSettingActivity.this, "workname");
        workID = getIntent().getStringExtra("id");
        txt_project_header.setText(name);
        ll_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProjectSettingActivity.this, NewProjctActivity.class);
                intent.putExtra("id", AppConstants.loadPreferences(ProjectSettingActivity.this, "workid"));
                startActivity(intent);
            }
        });
        img_project_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(ProjectSettingActivity.this, "aman", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (NetworkAvailablity.chkStatus(ProjectSettingActivity.this)) {
            getprojectlist();

        } else {
            Toast.makeText(ProjectSettingActivity.this, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void getprojectlist() {


        // load data here
        projectList.clear();


        ParseQuery<ParseObject> query = ParseQuery.getQuery("Project");
//        query.whereEqualTo("workspaceID", workID);
        query.whereEqualTo("workspace", ParseObject.createWithoutData("WorkSpace", workID));
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

                        ParseFile image = (ParseFile) objects.get(i).get("image");
                        String proImage = image.getUrl();
                        String type = objects.get(i).getString("type");
                        //childList.add("")
                        if (!name.equals("General") && !name.equals("My Notes")) {
                            projectList.add(new ProjctBean(objectId, name, updatedAt, workspaceID, objective, createdAt, proImage, type, objects.get(i)));
                        }
                    }

                    adapter = new ProjectAdapter(ProjectSettingActivity.this, projectList, "");
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ProjectSettingActivity.this);
                    recycleProject.setLayoutManager(layoutManager);
                    // recycleProject.addItemDecoration(new SimpleDividerItemDecoration(ProjectSettingActivity.this));
                    recycleProject.setAdapter(adapter);


                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ProjectSettingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    Log.e("lse", e.getMessage());
                    // error
                }

            }
        });

    }

    private void findview() {
        projectList = new ArrayList<>();
        customFonts = new CustomFonts(ProjectSettingActivity.this);
        txt_project_header = (TextView) findViewById(R.id.txt_project_header);
        img_project_back = (ImageView) findViewById(R.id.projectback);
        recycleProject = (RecyclerView) findViewById(R.id.recycleProject);
        ll_add = (LinearLayout) findViewById(R.id.ll_add);
        txt_project_header.setTypeface(customFonts.CabinBold);
    }
}
