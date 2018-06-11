package com.samosys.paperai.activity.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.samosys.paperai.R;
import com.samosys.paperai.activity.Bean.ProjctBean;
import com.samosys.paperai.activity.adapter.CategoryProjectAdapter;
import com.samosys.paperai.activity.utils.AppConstants;
import com.samosys.paperai.activity.utils.CustomFonts;
import com.samosys.paperai.activity.utils.NetworkAvailablity;

import java.util.ArrayList;
import java.util.List;

public class CreateCategoryActivity extends AppCompatActivity {
    public static ArrayList<ParseObject> proList;
    CustomFonts customFonts;
    RecyclerView cateList;
    String workID = "";
    private ImageView img_catewgory_back;
    private TextView txt_workname_header;
    private ArrayList<ProjctBean> projectList;
    private EditText edt_catname;
    private LinearLayout ll_saveCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        AppConstants.getstatusbar(CreateCategoryActivity.this);
        workID = getIntent().getStringExtra("id");

        findview();

        if (NetworkAvailablity.chkStatus(CreateCategoryActivity.this)) {

            getprojectlist();

        } else {
            Toast.makeText(CreateCategoryActivity.this, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
        }

        ll_saveCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edt_catname.getText().toString();
                if (name.equals("")) {
                    Toast.makeText(CreateCategoryActivity.this, "Please enter category name", Toast.LENGTH_SHORT).show();
                } else if (proList.size()<=0){
                    saveCategory();
                }
            }
        });
        img_catewgory_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void saveCategory() {

        final ProgressDialog dialog = AppConstants.showProgressDialog(CreateCategoryActivity.this, "Please wait...");

        final ParseObject gameScore = new ParseObject("Category");
          gameScore.put("user", ParseObject.createWithoutData("_User", ParseUser.getCurrentUser().getObjectId()));
//
        gameScore.put("name", edt_catname.getText().toString());
//        gameScore.include("post_id.user_id");
        gameScore.put("workspaceID", ParseObject.createWithoutData("WorkSpace", workID));
        gameScore.add("Projects",proList );

        gameScore.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (e == null) {

                    Toast.makeText(CreateCategoryActivity.this, "succes", Toast.LENGTH_SHORT).show();
                    finish();
//                    for (int a = 0; a < proList.size(); a++) {
//                        ParseQuery<ParseObject> query = ParseQuery.getQuery("Project");
//                      //  query.include("category.objectId");
//                        query.whereEqualTo("workspace",ParseObject.createWithoutData("WorkSpace",workID));
//                        query.getInBackground(proList.get(a), new GetCallback<ParseObject>() {
//                            @Override
//                            public void done(ParseObject object, ParseException e) {
//                                if (e == null) {
//
//                                    object.put("category", ParseObject.createWithoutData("Category", gameScore.getObjectId()));
//                                    object.saveInBackground();
//                                    Toast.makeText(CreateCategoryActivity.this, "succes", Toast.LENGTH_SHORT).show();
//                                finish();
//                                }
//                            }
//                        });
//                    }


                } else {

                    Toast.makeText(CreateCategoryActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void getprojectlist() {


        // load data here
        projectList.clear();


        ParseQuery<ParseObject> query = ParseQuery.getQuery("Project");
        query.whereEqualTo("workspace",ParseObject.createWithoutData("WorkSpace",workID));

        query.include("category");
        //query.whereEqualTo("category", ParseObject.createWithoutData("Category", "null"));
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {


                    Log.e("WORKEDID", "d>>>>>" + objects.size());
                    for (int i = 0; i < objects.size(); i++) {

                        ParseObject parseObject = objects.get(i).getParseObject("category");
                        if (parseObject==null) {
//                            String name1 = parseObject.getString("name");
//                            Log.e("category11", name1);
                            String objectId = objects.get(i).getObjectId();

                            String name = objects.get(i).getString("name");
                            String updatedAt = objects.get(i).getString("updatedAt");
                            String workspaceID = objects.get(i).getString("workspaceID");
                            String objective = objects.get(i).getString("objective");
                            String createdAt = objects.get(i).getString("createdAt");
                            ParseFile proImage = (ParseFile) objects.get(i).get("image");
                            String image = proImage.getUrl();
                            String type = objects.get(i).getString("type");
                           // String type = objects.get(i).getString("type");
//                        String type = objects.get(i).getString("category");
                            //childList.add("")
                            //if (!name.equals("General") && !name.equals("My Notes")) {
                                projectList.add(new ProjctBean(objectId, name, updatedAt, workspaceID, objective, createdAt, image, type,objects.get(i)));
//                            }

                        }
                    }



                    CategoryProjectAdapter categoryProjectAdapter = new CategoryProjectAdapter(CreateCategoryActivity.this, projectList);
                    cateList.setHasFixedSize(true);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(CreateCategoryActivity.this);
                    cateList.setLayoutManager(layoutManager);
                    cateList.setAdapter(categoryProjectAdapter);
                    categoryProjectAdapter.notifyDataSetChanged();


                } else {
                    Toast.makeText(CreateCategoryActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    Log.e("lse", e.getMessage());
                    // error
                }

            }
        });

    }

    private void findview() {
        customFonts = new CustomFonts(CreateCategoryActivity.this);
        projectList = new ArrayList<>();

        proList = new ArrayList<>();
        img_catewgory_back=(ImageView)findViewById(R.id.img_catewgory_back);
        txt_workname_header = (TextView) findViewById(R.id.txt_workname_header);
        cateList = (RecyclerView) findViewById(R.id.cateList);
        edt_catname = (EditText) findViewById(R.id.edt_catname);
        ll_saveCategory = (LinearLayout) findViewById(R.id.ll_saveCategory);


        txt_workname_header.setTypeface(customFonts.CabinBold);
    }
}
