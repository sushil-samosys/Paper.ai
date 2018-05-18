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
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.samosys.paperai.R;
import com.samosys.paperai.activity.Bean.CategoryList;
import com.samosys.paperai.activity.adapter.CategoryListAdapter;
import com.samosys.paperai.activity.utils.AppConstants;
import com.samosys.paperai.activity.utils.CustomFonts;
import com.samosys.paperai.activity.utils.NetworkAvailablity;

import java.util.ArrayList;
import java.util.List;

public class CategoryListingActivity extends AppCompatActivity {
    CustomFonts customFonts;
    private RecyclerView recycleCategory;
    private String workID = "", workname = "";
    private ArrayList<CategoryList> categoryList;
    private LinearLayout ll_addCategy;
    private ImageView img_work_space_back;
    private TextView txt_workname_header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppConstants.getstatusbar(CategoryListingActivity.this);
        setContentView(R.layout.activity_category_listing);
        workID = getIntent().getStringExtra("id");
        workname = getIntent().getStringExtra("name");

        findview();
        txt_workname_header.setText(workname);

        ll_addCategy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryListingActivity.this, CreateCategoryActivity.class);
                intent.putExtra("id", workID);
                startActivity(intent);
            }
        });
        img_work_space_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (NetworkAvailablity.chkStatus(CategoryListingActivity.this)) {

            getcategorylisting();

        } else {
            Toast.makeText(CategoryListingActivity.this, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void getcategorylisting() {
        categoryList.clear();

        Log.e("workID", workID + "");

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Category");
        query.whereEqualTo("workspaceID", ParseObject.createWithoutData("WorkSpace", workID));

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    Log.e("ERROR", objects.size() + "");

                    for (int i = 0; i < objects.size(); i++) {
                        String objectId = objects.get(i).getObjectId();
                        String name = objects.get(i).getString("name");
                        String updatedAt = objects.get(i).getString("updatedAt");
                        String workspaceID = objects.get(i).getString("workspaceID");
                        String createdAt = objects.get(i).getString("createdAt");


                        categoryList.add(new CategoryList(objectId, name, updatedAt, workspaceID, createdAt));
                    }


                    CategoryListAdapter categoryProjectAdapter = new CategoryListAdapter(CategoryListingActivity.this, categoryList);
                    recycleCategory.setHasFixedSize(true);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(CategoryListingActivity.this);
                    recycleCategory.setLayoutManager(layoutManager);
                    recycleCategory.setAdapter(categoryProjectAdapter);
                    //    recycleCategory.addItemDecoration(new SimpleDividerItemDecoration(CategoryListingActivity.this));
                    categoryProjectAdapter.notifyDataSetChanged();


                } else {
                    Toast.makeText(CategoryListingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    Log.e("ERROR", e.getMessage());
                    // error
                }

            }
        });

    }

    private void findview() {
        customFonts = new CustomFonts(CategoryListingActivity.this);
        txt_workname_header = (TextView) findViewById(R.id.txt_workname_header);
        recycleCategory = (RecyclerView) findViewById(R.id.recycleCategory);
        ll_addCategy = (LinearLayout) findViewById(R.id.ll_addCategy);
        img_work_space_back = (ImageView) findViewById(R.id.img_work_space_back);
        categoryList = new ArrayList<>();
        txt_workname_header.setTypeface(customFonts.CabinBold);
    }
}
