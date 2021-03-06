package com.samosys.paperai.activity.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
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
import com.samosys.paperai.activity.utils.CustomFonts;
import com.samosys.paperai.activity.utils.NetworkAvailablity;
import com.samosys.paperai.activity.utils.RecyclerTouchListener;
import com.samosys.paperai.activity.utils.SwitchButton;

import java.util.ArrayList;
import java.util.List;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import cn.zjy.actionsheet.ActionSheet;

public class ArchiveWorkspaceActivity extends AppCompatActivity {
    private CustomFonts customFonts;
    private RecyclerView listChannels;
    private String workID = "";
    private TextView txtsave;
    private SwitchButton toglBtn;
    private String toggle = "";
    private ImageView archiveWStback;
    private ArchiveProjectAdapter adapter;
    private TextView txtEntiretxt, txtChaneelstxt, txt_msg1, txt_msg2;
    private ArrayList<ProjctBean> projectList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive_workspace);
        AppConstants.getstatusbar(ArchiveWorkspaceActivity.this);

        Findviews();

        listChannels.addOnItemTouchListener(new RecyclerTouchListener(ArchiveWorkspaceActivity.this, listChannels, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                delete(position);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        toglBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    toggle = "1";


                } else {
                    toggle = "0";


                }
            }
        });

        txtsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkAvailablity.chkStatus(ArchiveWorkspaceActivity.this)) {
                    updatearchive();

                } else {
                    Toast.makeText(ArchiveWorkspaceActivity.this, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                }

            }
        });
        archiveWStback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void delete(final int position) {
        ActionSheet actionSheet = new ActionSheet.Builder()

                .setOtherBtn(new String[]{"Retrive Project"}, new int[]{Color.parseColor("#4C4C4C"), Color.parseColor("#2dc8bc")})


                .setCancelBtn("Cancel", Color.parseColor("#4C4C4C"))

                .setCancelableOnTouchOutside(true)
                .setActionSheetListener(new ActionSheet.ActionSheetListener() {
                    @Override
                    public void onDismiss(ActionSheet actionSheet, boolean isByBtn) {

                    }

                    @Override
                    public void onButtonClicked(ActionSheet actionSheet, int index) {
                        if (index == 0) {
                            retiveproject(position);
                        }

                    }
                }).build();

        actionSheet.show(ArchiveWorkspaceActivity.this.getFragmentManager());
    }

    private void retiveproject(int position) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Project");
        query.whereEqualTo("objectId", projectList.get(position).getObjectId());
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    ParseObject person = list.get(0);
                    person.put("archive", "0");
                    if (NetworkAvailablity.chkStatus(ArchiveWorkspaceActivity.this)) {
                        projectList.clear();
                        getprojectlist();


                    } else {
                        Toast.makeText(ArchiveWorkspaceActivity.this, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                    }

                    person.saveInBackground();
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });
    }

    private void updatearchive() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("WorkSpace");
        query.whereEqualTo("objectId", workID);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    ParseObject person = list.get(0);
                    person.put("archive", toggle);
                    Intent intent = new Intent(ArchiveWorkspaceActivity.this, HomeFeedActivity.class);
                    startActivity(intent);

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
        if (NetworkAvailablity.chkStatus(ArchiveWorkspaceActivity.this)) {
            getprojectlist();

        } else {
            Toast.makeText(ArchiveWorkspaceActivity.this, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
        }


    }


    private void getprojectlist() {

        final ACProgressFlower dialog = new ACProgressFlower.Builder(this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)

                .fadeColor(Color.DKGRAY).build();
        dialog.show();
        // load data here
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
                            if (archive.equals("1")) {
                                projectList.add(new ProjctBean(objectId, name, updatedAt, workspaceID, objective, createdAt, proImage, type, objects.get(i), archive));
                            }
                        }
                    }

                    adapter = new ArchiveProjectAdapter(ArchiveWorkspaceActivity.this, projectList, "");
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ArchiveWorkspaceActivity.this);
                    listChannels.setLayoutManager(layoutManager);
                    listChannels.setAdapter(adapter);


                    adapter.notifyDataSetChanged();


                } else {
                    Toast.makeText(ArchiveWorkspaceActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();


                }
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }

            }
        });

    }

    private void Findviews() {
        projectList = new ArrayList<>();
        workID = AppConstants.loadPreferences(ArchiveWorkspaceActivity.this, "workid");
        customFonts = new CustomFonts(ArchiveWorkspaceActivity.this);
        txtEntiretxt = (TextView) findViewById(R.id.txtEntiretxt);
        txtChaneelstxt = (TextView) findViewById(R.id.txtChaneelstxt);
        txt_msg1 = (TextView) findViewById(R.id.txt_msg1);
        txt_msg2 = (TextView) findViewById(R.id.txt_msg2);
        archiveWStback = (ImageView) findViewById(R.id.archiveWStback);
        txtsave = (TextView) findViewById(R.id.txtsave);
        toglBtn = (SwitchButton) findViewById(R.id.toglBtn);
        listChannels = (RecyclerView) findViewById(R.id.listChannels);
        listChannels.setNestedScrollingEnabled(false);
        txtEntiretxt.setTypeface(customFonts.CabinBold);
        txtChaneelstxt.setTypeface(customFonts.CabinBold);
        txt_msg1.setTypeface(customFonts.CabinRegular);
        txt_msg2.setTypeface(customFonts.CabinRegular);
        txtsave.setTypeface(customFonts.CabinRegular);
    }


}
