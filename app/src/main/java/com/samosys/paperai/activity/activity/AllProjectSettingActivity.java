package com.samosys.paperai.activity.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.samosys.paperai.R;
import com.samosys.paperai.activity.Bean.ProjctBean;
import com.samosys.paperai.activity.adapter.ArchiveProjectAdapter;
import com.samosys.paperai.activity.utils.AppConstants;
import com.samosys.paperai.activity.utils.NetworkAvailablity;
import com.samosys.paperai.activity.utils.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import cn.zjy.actionsheet.ActionSheet;

public class AllProjectSettingActivity extends AppCompatActivity {
    private RelativeLayout rlAdminsonly, rlmembersonly, rlEveryonesonly;
    private ImageView ImgEveryonesonly, Imgmembersonly, ImgAdminsonly;
    private String proID = "", workID = "";
    private TextView txtCrewatePro;
    private RecyclerView rvChannels;
    private ArchiveProjectAdapter adapter;
    private ArrayList<ProjctBean> projectList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive_project);
        AppConstants.getstatusbar(AllProjectSettingActivity.this);

        findview();

        rlAdminsonly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImgEveryonesonly.setVisibility(View.INVISIBLE);
                Imgmembersonly.setVisibility(View.INVISIBLE);
                ImgAdminsonly.setVisibility(View.VISIBLE);

            }
        });
        rlmembersonly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImgEveryonesonly.setVisibility(View.INVISIBLE);
                Imgmembersonly.setVisibility(View.VISIBLE);
                ImgAdminsonly.setVisibility(View.INVISIBLE);

            }
        });
        rlEveryonesonly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImgEveryonesonly.setVisibility(View.VISIBLE);
                Imgmembersonly.setVisibility(View.INVISIBLE);
                ImgAdminsonly.setVisibility(View.INVISIBLE);

            }
        });
        txtCrewatePro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllProjectSettingActivity.this, NewProjctActivity.class);
                intent.putExtra("id", AppConstants.loadPreferences(AllProjectSettingActivity.this, "workid"));
                startActivity(intent);

            }
        });
        rvChannels.addOnItemTouchListener(new RecyclerTouchListener(AllProjectSettingActivity.this, rvChannels, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                delete(position);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }

    private void delete(final int position) {
        ActionSheet actionSheet = new ActionSheet.Builder()
                .setOtherBtn(new String[]{"Archive Project", "View Project"}, new int[]{Color.parseColor("#4C4C4C"), Color.parseColor("#4C4C4C")})


                .setCancelBtn("Cancel", Color.parseColor("#4C4C4C"))

                .setCancelableOnTouchOutside(true)
                .setActionSheetListener(new ActionSheet.ActionSheetListener() {
                    @Override
                    public void onDismiss(ActionSheet actionSheet, boolean isByBtn) {

                    }

                    @Override
                    public void onButtonClicked(ActionSheet actionSheet, int index) {
                        if (index == 0) {

                            archiveproject();
                        } else if (index == 1) {
                            Intent intent = new Intent(AllProjectSettingActivity.this, ProjectDetailActivity.class);
                            intent.putExtra("id", projectList.get(position).getObjectId()
                            );
                            startActivity(intent);
                        }

                    }
                }).build();

        actionSheet.show(AllProjectSettingActivity.this.getFragmentManager());
    }

    private void archiveproject() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Project");
        query.whereEqualTo("objectId", proID);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    ParseObject person = list.get(0);
                    person.put("archive", "1");
                    if (NetworkAvailablity.chkStatus(AllProjectSettingActivity.this)) {
                        getprojectlist();

                    } else {
                        Toast.makeText(AllProjectSettingActivity.this, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                    }

                    person.saveInBackground();
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (NetworkAvailablity.chkStatus(AllProjectSettingActivity.this)) {
            getprojectlist();

        } else {
            Toast.makeText(AllProjectSettingActivity.this, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void getprojectlist() {
        final ACProgressFlower dialog = new ACProgressFlower.Builder(this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)

                .fadeColor(Color.DKGRAY).build();
        dialog.show();
        projectList.clear();


        ParseQuery<ParseObject> query = ParseQuery.getQuery("Project");

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
                        String archive = objects.get(i).getString("archive");


                        if (objects.get(i).has("default")) {
                            String state = objects.get(i).getString("default");


                        } else {
                            if (archive.equals("0")) {
                                projectList.add(new ProjctBean(objectId, name, updatedAt, workspaceID, objective, createdAt, proImage, type, objects.get(i), archive));
                            }
                        }
                    }

                    adapter = new ArchiveProjectAdapter(AllProjectSettingActivity.this, projectList, "");
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(AllProjectSettingActivity.this);
                    rvChannels.setLayoutManager(layoutManager);
                    rvChannels.setAdapter(adapter);
                    adapter.notifyDataSetChanged();


                } else {
                    Toast.makeText(AllProjectSettingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }

            }
        });

    }

    private void findview() {
        proID = getIntent().getStringExtra("id");
        projectList = new ArrayList<>();
        workID = AppConstants.loadPreferences(AllProjectSettingActivity.this, "workid");
        rlAdminsonly = (RelativeLayout) findViewById(R.id.rlAdminsonly);
        rlmembersonly = (RelativeLayout) findViewById(R.id.rlmembersonly);
        rlEveryonesonly = (RelativeLayout) findViewById(R.id.rlEveryonesonly);
        ImgEveryonesonly = (ImageView) findViewById(R.id.ImgEveryonesonly);
        Imgmembersonly = (ImageView) findViewById(R.id.Imgmembersonly);
        ImgAdminsonly = (ImageView) findViewById(R.id.ImgAdminsonly);
        rvChannels = (RecyclerView) findViewById(R.id.rvChannels);
        txtCrewatePro = (TextView) findViewById(R.id.txtCrewatePro);
    }
}
